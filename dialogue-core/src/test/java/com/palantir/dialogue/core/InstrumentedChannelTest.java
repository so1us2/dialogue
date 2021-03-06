/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.google.common.util.concurrent.Futures;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.Endpoint;
import com.palantir.tritium.metrics.registry.DefaultTaggedMetricRegistry;
import com.palantir.tritium.metrics.registry.TaggedMetricRegistry;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("FutureReturnValueIgnored")
public final class InstrumentedChannelTest {

    private TaggedMetricRegistry registry;
    private InstrumentedChannel channel;

    @Mock
    private Channel delegate;

    @Mock
    private Endpoint endpoint;

    @BeforeEach
    public void before() {
        registry = new DefaultTaggedMetricRegistry();
        channel = new InstrumentedChannel(delegate, "my-channel", registry);
    }

    @Test
    public void addsMetricsForSuccessfulExecution() {
        when(endpoint.serviceName()).thenReturn("my-service");

        ClientMetrics metrics = ClientMetrics.of(registry);
        Timer timer = metrics.response()
                .channelName("my-channel")
                .serviceName(endpoint.serviceName())
                .build();
        Meter responseErrors = metrics.responseError()
                .channelName("my-channel")
                .serviceName(endpoint.serviceName())
                .reason(IOException.class.getSimpleName())
                .build();

        assertThat(timer.getCount()).isZero();

        // Successful execution
        when(delegate.execute(any(), any())).thenReturn(Futures.immediateFuture(null));
        channel.execute(endpoint, null);
        assertThat(timer.getCount()).isEqualTo(1);
        assertThat(responseErrors.getCount()).isZero();
    }

    @Test
    public void addsMetricsForUnsuccessfulExecution_runtimeException() {
        when(endpoint.serviceName()).thenReturn("my-service");
        ClientMetrics metrics = ClientMetrics.of(registry);
        Timer timer = metrics.response()
                .channelName("my-channel")
                .serviceName(endpoint.serviceName())
                .build();
        Meter responseErrors = metrics.responseError()
                .channelName("my-channel")
                .serviceName(endpoint.serviceName())
                .reason(IOException.class.getSimpleName())
                .build();

        assertThat(timer.getCount()).isZero();
        assertThat(responseErrors.getCount()).isZero();

        // Unsuccessful execution with IOException
        when(delegate.execute(any(), any())).thenReturn(Futures.immediateFailedFuture(new RuntimeException()));
        channel.execute(endpoint, null);
        assertThat(timer.getCount()).isEqualTo(1);
        assertThat(responseErrors.getCount()).isZero();
    }

    @Test
    public void addsMetricsForUnsuccessfulExecution_ioException() {
        when(endpoint.serviceName()).thenReturn("my-service");
        ClientMetrics metrics = ClientMetrics.of(registry);
        Timer timer = metrics.response()
                .channelName("my-channel")
                .serviceName(endpoint.serviceName())
                .build();
        Meter responseErrors = metrics.responseError()
                .channelName("my-channel")
                .serviceName(endpoint.serviceName())
                .reason(IOException.class.getSimpleName())
                .build();

        assertThat(timer.getCount()).isZero();
        assertThat(responseErrors.getCount()).isZero();

        // Unsuccessful execution with IOException
        when(delegate.execute(any(), any())).thenReturn(Futures.immediateFailedFuture(new IOException()));
        channel.execute(endpoint, null);
        assertThat(timer.getCount()).isEqualTo(1);
        assertThat(responseErrors.getCount()).isOne();
    }
}
