package dev.ash.techradar.common.error.util;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.UUID;

/**
 * Utility class for generating and managing trace IDs.
 */
@UtilityClass
public class TraceIdGenerator {

    private static final String TRACE_ID_KEY = "traceId";

    private static final String TRACE_ID_PREFIX = "TR-";

    /**
     * Generates a new trace ID.
     */
    public String generateTraceId() {
        return TRACE_ID_PREFIX + UUID.randomUUID().toString();
    }

    /**
     * Generates a time-based trace ID.
     */
    public String generateTimeBasedTraceId() {
        return TRACE_ID_PREFIX +
            Instant.now().toEpochMilli() + "-" +
            UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Gets the current trace ID from MDC or generates a new one.
     */
    public String getCurrentTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        if (traceId == null) {
            traceId = generateTraceId();
            MDC.put(TRACE_ID_KEY, traceId);
        }
        return traceId;
    }

    /**
     * Sets a new trace ID in the MDC.
     */
    public String setNewTraceId() {
        String traceId = generateTraceId();
        MDC.put(TRACE_ID_KEY, traceId);
        return traceId;
    }

    /**
     * Clears the trace ID from MDC.
     */
    public void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }
}
