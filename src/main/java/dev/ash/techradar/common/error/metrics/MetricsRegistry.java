package dev.ash.techradar.common.error.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Registry for managing and tracking various metrics in the Tech Radar application.
 */
@Component
@RequiredArgsConstructor
public class MetricsRegistry {

    private final MeterRegistry meterRegistry;

    private final Map<String, Timer> operationTimers = new ConcurrentHashMap<>();

    /**
     * Records the duration of an operation.
     */
    public void recordOperationDuration(String operation, long duration, TimeUnit unit) {
        getOrCreateTimer(operation).record(duration, unit);
    }

    /**
     * Creates a timer for measuring operation duration.
     */
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Stops and records a timer for an operation.
     */
    public void stopTimer(Timer.Sample sample, String operation) {
        sample.stop(getOrCreateTimer(operation));
    }

    /**
     * Gets or creates a timer for an operation.
     */
    private Timer getOrCreateTimer(String operation) {
        return operationTimers.computeIfAbsent(operation,
                                               op -> Timer.builder("techradar.operation.duration")
                                                   .tag("operation", op)
                                                   .register(meterRegistry));
    }
}
