# Metrics

## Dialogue Apache Hc4 Client

`com.palantir.dialogue:dialogue-apache-hc4-client`

### dialogue.client
Dialogue client response metrics provided by the Apache client channel.
- `dialogue.client.response.leak` tagged `client-name`, `service-name`, `endpoint` (meter): Rate that responses are garbage collected without being closed. This should only occur in the case of a programming error.
- `dialogue.client.create` tagged `client-name`, `client-type` (meter): Marked every time a new client is created.
- `dialogue.client.close` tagged `client-name`, `client-type` (meter): Marked every time an Apache client is successfully closed and any underlying resources released (e.g. connections and background threads).

### dialogue.client.pool
Connection pool metrics from the dialogue Apache client.
- `dialogue.client.pool.size` tagged `client-name`, `state` (gauge): Number of connections in the client connection pool in states `idle`, `pending`, and `leased`.

## Dialogue Core

`com.palantir.dialogue:dialogue-core`

### client
General client metrics produced by dialogue. These metrics are meant to be applicable to all conjure clients without being implementation-specific.
- `client.response` tagged `channel-name`, `service-name` (timer): Request time, note that this does not include time spent reading the response body.
- `client.response.error` tagged `channel-name`, `service-name`, `reason` (meter): Rate of errors received by reason and service-name. Currently only errors with reason `IOException` are reported.
- `client.deprecations` tagged `service-name` (meter): Rate of deprecated endpoints being invoked.

### dialogue.balanced
Instrumentation for BalancedChannel internals.
- `dialogue.balanced.score` tagged `channel-name`, `hostIndex` (gauge): The score that the BalancedChannel currently assigns to each host (computed based on inflight requests and recent failures). Requests are routed to the channel with the lowest score. (Note if there are >10 nodes this metric will not be recorded).

### dialogue.client
Dialogue-specific metrics that are not necessarily applicable to other client implementations.
- `dialogue.client.response.leak` tagged `client-name`, `service-name`, `endpoint` (meter): Rate that responses are garbage collected without being closed. This should only occur in the case of a programming error.
- `dialogue.client.request.active` tagged `channel-name`, `service-name`, `stage` (counter): Number of requests that are actively running. The `stage` may refer to `running` requests actively executing over the wire or `processing` which may be awaiting a client or backing off for a retry. Note that running requests are also counted as processing.
- `dialogue.client.request.retry` tagged `channel-name`, `reason` (meter): Rate at which the RetryingChannel retries requests (across all endpoints).
- `dialogue.client.requests.queued` tagged `channel-name` (counter): Number of queued requests waiting to execute.
- `dialogue.client.request.queued.time` tagged `channel-name` (timer): Time spent waiting in the queue before execution.
- `dialogue.client.limited` tagged `channel-name`, `reason` (meter): Rate that client-side requests are deferred to be retried later.
- `dialogue.client.create` tagged `client-name`, `client-type` (meter): Marked every time a new client is created.

### dialogue.concurrencylimiter
Instrumentation for the ConcurrencyLimitedChannel
- `dialogue.concurrencylimiter.max` tagged `channel-name`, `hostIndex` (gauge): The maximum number of concurrent requests which are currently permitted. Additively increases with successes and multiplicatively decreases with failures.

### dialogue.nodeselection
Instrumentation for which node selection strategy is used
- `dialogue.nodeselection.strategy` tagged `channel-name`, `strategy` (meter): Marked every time the node selection strategy changes

### dialogue.pinuntilerror
Instrumentation for the PIN_UNTIL_ERROR node selection strategy.
- `dialogue.pinuntilerror.success` tagged `channel-name`, `hostIndex` (meter): Meter of the requests that were successfully made, tagged by the index of the inner channel. (Note if there are >10 nodes this metric will not be recorded).
- `dialogue.pinuntilerror.nextNode` tagged `channel-name`, `reason` (meter): Marked every time we switch to a new node, includes the reason why we switched (limited, responseCode, throwable).
- `dialogue.pinuntilerror.reshuffle` tagged `channel-name` (meter): Marked every time we reshuffle all the nodes.

### dialogue.roundrobin
Instrumentation for the ROUND_ROBIN node selection strategy (currently implemented by BalancedChannel).
- `dialogue.roundrobin.success` tagged `channel-name`, `hostIndex` (meter): Meter of the requests that were successfully made, tagged by the index of the host. (Note if there are >10 nodes this metric will not be recorded).

## Tritium Metrics

`com.palantir.tritium:tritium-metrics`

### executor
Executor metrics.
- `executor.submitted` tagged `executor` (meter): A meter of the number of submitted tasks.
- `executor.running` tagged `executor` (counter): A gauge of the number of running tasks.
- `executor.completed` tagged `executor` (meter): A meter of the number of completed tasks.
- `executor.duration` tagged `executor` (timer): A timer of the time it took to run a task.
- `executor.queued-duration` tagged `executor` (timer): A timer of the time it took a task to start running after it was submitted.
- `executor.scheduled.once` tagged `executor` (meter): A meter of the number of one-shot scheduled tasks. Applies only to scheduled executors.
- `executor.scheduled.repetitively` tagged `executor` (meter): A meter of the number of repetitive scheduled tasks. Applies only to scheduled executors.
- `executor.scheduled.overrun` tagged `executor` (counter): A gauge of the number of fixed-rate scheduled tasks that overran the scheduled rate. Applies only to scheduled executors.
- `executor.scheduled.percent-of-period` tagged `executor` (histogram): A histogram of the time it took to run a fixed-rate scheduled task as a percentage of the scheduled rate. Applies only to scheduled executors.
- `executor.threads.created` tagged `executor` (meter): Rate that new threads are created for this executor.
- `executor.threads.terminated` tagged `executor` (meter): Rate that executor threads are terminated.
- `executor.threads.running` tagged `executor` (counter): Number of live threads created by this executor.

### jvm.gc
Java virtual machine garbage collection metrics.
- `jvm.gc.count` tagged `collector` (gauge): The total number of collections that have occurred since the JVM started.
- `jvm.gc.time` tagged `collector` (gauge): The accumulated collection elapsed time in milliseconds.
- `jvm.gc.finalizer.queue.size` (gauge): Estimate of the number of objects pending finalization. Finalizers are executed in serial on a single thread shared across the entire JVM. When a finalizer is slow and blocks execution, or objects which override `Object.finalize` are allocated more quickly than they can be freed, the JVM will run out of memory. Cleaners are recommended over implementing finalize in most scenarios.

### jvm.memory.pools
Java virtual machine memory usage metrics by memory pool.
- `jvm.memory.pools.max` tagged `memoryPool` (gauge): Gauge of the maximum number of bytes that can be used by the corresponding pool.
- `jvm.memory.pools.used` tagged `memoryPool` (gauge): Gauge of the number of bytes used by the corresponding pool.
- `jvm.memory.pools.committed` tagged `memoryPool` (gauge): Gauge of the number of bytes that the jvm has committed to use by the corresponding pool.
- `jvm.memory.pools.init` tagged `memoryPool` (gauge): Gauge of the number of bytes that the jvm initially requested to the os by the corresponding pool.
- `jvm.memory.pools.usage` tagged `memoryPool` (gauge): Gauge of the ratio of the number of bytes used to the maximum number of bytes that can be used by the corresponding pool.
- `jvm.memory.pools.used-after-gc` tagged `memoryPool` (gauge): Gauge of the number of bytes used after the last garbage collection by the corresponding pool. Note that this metrics is not supported by all implementations.

### tls
Transport layer security metrics.
- `tls.handshake` tagged `context`, `cipher`, `protocol` (meter): Measures the rate of TLS handshake by SSLContext, cipher suite, and TLS protocol. A high rate of handshake suggests that clients are not properly reusing connections, which results in additional CPU overhead and round trips.