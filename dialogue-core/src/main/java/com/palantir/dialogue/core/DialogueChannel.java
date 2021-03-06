/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.dialogue.core;

import com.codahale.metrics.Meter;
import com.github.benmanes.caffeine.cache.Ticker;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.client.config.ClientConfiguration;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Response;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DialogueChannel implements Channel {
    private static final Logger log = LoggerFactory.getLogger(DialogueChannel.class);
    private final Channel delegate;

    // we keep around internals purely for live-reloading
    private final Config cf;

    private final QueuedChannel queuedChannel; // just so we can process the queue when uris reload
    private final Map<String, LimitedChannel> limitedChannelByUri = new ConcurrentHashMap<>();
    private final NodeSelectionStrategyChannel nodeSelectionChannel;

    private DialogueChannel(Config cf) {
        this.cf = withUris(cf, Collections.emptyList()); // zeroing these out because this isn't the source of truth
        this.nodeSelectionChannel = NodeSelectionStrategyChannel.create(cf);
        this.queuedChannel = QueuedChannel.create(cf, nodeSelectionChannel);
        this.delegate = wrapQueuedChannel(cf, queuedChannel);
        updateUrisInner(cf.clientConf().uris(), true);
    }

    private static ImmutableConfig withUris(Config cf, List<String> elements) {
        return ImmutableConfig.builder()
                .from(cf)
                .rawConfig(ClientConfiguration.builder()
                        .from(cf.clientConf())
                        .uris(elements)
                        .build())
                .build();
    }

    private static LimitedChannel createPerUriChannel(Config cf, String uri) {
        Channel channel = cf.channelFactory().create(uri);
        // Instrument inner-most channel with instrumentation channels so that we measure only the over-the-wire-time
        channel = new InstrumentedChannel(
                channel, cf.channelName(), cf.clientConf().taggedMetricRegistry());
        channel = HostMetricsChannel.create(channel, cf, uri);
        channel = new ActiveRequestInstrumentationChannel(
                channel, cf.channelName(), "running", cf.clientConf().taggedMetricRegistry());
        // TracedChannel must wrap TracedRequestChannel to ensure requests have tracing headers.
        channel = new TraceEnrichingChannel(channel);

        ChannelToLimitedChannelAdapter limited = new ChannelToLimitedChannelAdapter(channel);
        return ConcurrencyLimitedChannel.create(
                cf, limited, cf.clientConf().uris().indexOf(uri));
    }

    private static Channel wrapQueuedChannel(Config cf, QueuedChannel queuedChannel) {
        Channel channel = new TracedChannel(queuedChannel, "Dialogue-request-attempt");
        channel = RetryingChannel.create(cf, channel);
        channel = new UserAgentChannel(channel, cf.clientConf().userAgent().get());
        channel = new DeprecationWarningChannel(channel, cf.clientConf().taggedMetricRegistry());
        channel = new ContentDecodingChannel(channel);
        channel = new DialogueTracedRequestChannel(channel);
        channel = new ActiveRequestInstrumentationChannel(
                channel, cf.channelName(), "processing", cf.clientConf().taggedMetricRegistry());
        channel = new NeverThrowChannel(channel); // this must come last as a defensive backstop
        return channel;
    }

    @Override
    public ListenableFuture<Response> execute(Endpoint endpoint, Request request) {
        return delegate.execute(endpoint, request);
    }

    public void updateUris(Collection<String> uris) {
        updateUrisInner(uris, false);
    }

    private void updateUrisInner(Collection<String> uris, boolean firstTime) {
        Set<String> uniqueUris = new HashSet<>(uris);
        // Uris didn't really change so nothing to do
        if (limitedChannelByUri.keySet().equals(uniqueUris) && !firstTime) {
            return;
        }

        infoLogUriUpdate(uris, firstTime);

        Sets.SetView<String> staleUris = Sets.difference(limitedChannelByUri.keySet(), uniqueUris);
        Sets.SetView<String> newUris = Sets.difference(uniqueUris, limitedChannelByUri.keySet());

        staleUris.forEach(limitedChannelByUri::remove);
        ImmutableList<String> allUris = ImmutableList.<String>builder()
                .addAll(limitedChannelByUri.keySet())
                .addAll(newUris)
                .build();

        newUris.forEach(uri -> {
            Config configWithUris = withUris(cf, allUris); // necessary for attribute metrics to the right hostIndex
            LimitedChannel singleUriChannel = createPerUriChannel(configWithUris, uri);
            limitedChannelByUri.put(uri, singleUriChannel);
        });

        nodeSelectionChannel.updateChannels(ImmutableList.copyOf(limitedChannelByUri.values()));
        // some queued requests might be able to make progress on a new uri now
        queuedChannel.schedule();
    }

    private void infoLogUriUpdate(Collection<String> uris, boolean firstTime) {
        if (!limitedChannelByUri.isEmpty() && uris.isEmpty()) {
            log.info(
                    "Updated to zero uris",
                    SafeArg.of("channelName", cf.channelName()),
                    SafeArg.of("prevNumUris", limitedChannelByUri.size()));
        }
        if (limitedChannelByUri.isEmpty() && !uris.isEmpty() && !firstTime) {
            log.info(
                    "Updated from zero uris",
                    SafeArg.of("channelName", cf.channelName()),
                    SafeArg.of("numUris", uris.size()));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "DialogueChannel@" + Integer.toHexString(System.identityHashCode(this)) + "{channelName="
                + cf.channelName() + ", delegate=" + delegate + '}';
    }

    public static final class Builder {
        private final ImmutableConfig.Builder builder = ImmutableConfig.builder();

        private Builder() {}

        /**
         * {@link Safe} loggable name to identify this channel for instrumentation and debugging. While this value
         * does not impact behavior, using a unique value for each channel makes it much easier to monitor and debug
         * the RPC stack.
         */
        public Builder channelName(@Safe String channelName) {
            builder.channelName(channelName);
            return this;
        }

        public Builder clientConfiguration(ClientConfiguration value) {
            builder.rawConfig(value);
            return this;
        }

        public Builder channelFactory(ChannelFactory value) {
            builder.channelFactory(value);
            return this;
        }

        @VisibleForTesting
        Builder random(Random value) {
            builder.random(value);
            return this;
        }

        @VisibleForTesting
        Builder scheduler(ScheduledExecutorService value) {
            builder.scheduler(value);
            return this;
        }

        @VisibleForTesting
        Builder maxQueueSize(int value) {
            builder.maxQueueSize(value);
            return this;
        }

        @VisibleForTesting
        Builder ticker(Ticker value) {
            builder.ticker(value);
            return this;
        }

        @CheckReturnValue
        public DialogueChannel build() {
            Config cf = builder.build();
            DialogueChannel dialogueChannel = new DialogueChannel(cf);

            Meter createMeter = DialogueClientMetrics.of(cf.clientConf().taggedMetricRegistry())
                    .create()
                    .clientName(cf.channelName())
                    .clientType("dialogue-channel")
                    .build();
            createMeter.mark();

            return dialogueChannel;
        }

        /** Does *not* do any clever live-reloading. */
        @CheckReturnValue
        public Channel buildNonLiveReloading() {
            Config cf = builder.build();

            ImmutableList<LimitedChannel> perUriChannels = cf.clientConf().uris().stream()
                    .map(uri -> DialogueChannel.createPerUriChannel(cf, uri))
                    .collect(ImmutableList.toImmutableList());

            NodeSelectionStrategyChannel nodeSelectionChannel = NodeSelectionStrategyChannel.create(cf);
            nodeSelectionChannel.updateChannels(perUriChannels);

            QueuedChannel queuedChannel = QueuedChannel.create(cf, nodeSelectionChannel);

            Channel channel = DialogueChannel.wrapQueuedChannel(cf, queuedChannel);

            Meter createMeter = DialogueClientMetrics.of(cf.clientConf().taggedMetricRegistry())
                    .create()
                    .clientName(cf.channelName())
                    .clientType("dialogue-channel-non-reloading")
                    .build();
            createMeter.mark();

            return channel;
        }
    }
}
