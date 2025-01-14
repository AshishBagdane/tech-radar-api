package dev.ash.techradar.common.error.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Component for tracking error metrics in the Tech Radar application.
 */
@Slf4j
@Component
public class ErrorMetrics {

    private final MeterRegistry meterRegistry;

    private final Map<String, Counter> errorCounters = new ConcurrentHashMap<>();

    private static final String ERROR_COUNTER_NAME = "techradar.errors";

    private static final String VALIDATION_COUNTER_NAME = "techradar.validation.errors";

    private static final String ERROR_TYPE_TAG = "error_type";

    private static final String ERROR_CODE_TAG = "error_code";

    private static final String DOMAIN_TAG = "domain";

    public ErrorMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Increments the error counter for a specific error type.
     */
    public void incrementErrorCount(String errorType) {
        getOrCreateErrorCounter(errorType).increment();
    }

    /**
     * Increments the error counter with additional context.
     */
    public void incrementErrorCount(String errorType, String errorCode, String domain) {
        List<Tag> tags = new ArrayList<>();
        tags.add(Tag.of(ERROR_TYPE_TAG, errorType));
        tags.add(Tag.of(ERROR_CODE_TAG, errorCode));
        tags.add(Tag.of(DOMAIN_TAG, domain));

        Counter counter = meterRegistry.counter(ERROR_COUNTER_NAME, tags);
        counter.increment();
    }

    /**
     * Records validation errors.
     */
    public void recordValidationErrors(String domain, int errorCount) {
        List<Tag> tags = new ArrayList<>();
        tags.add(Tag.of(DOMAIN_TAG, domain));

        Counter counter = meterRegistry.counter(VALIDATION_COUNTER_NAME, tags);
        counter.increment(errorCount);
    }

    /**
     * Gets or creates an error counter for a specific error type.
     */
    private Counter getOrCreateErrorCounter(String errorType) {
        return errorCounters.computeIfAbsent(errorType, type -> {
            List<Tag> tags = List.of(Tag.of(ERROR_TYPE_TAG, type));
            return meterRegistry.counter(ERROR_COUNTER_NAME, tags);
        });
    }
}
