package dev.ash.techradar.common.observability.trace.constants;

public final class TraceConstants {

    private TraceConstants() {}

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    public static final String CORRELATION_ID_KEY = "correlationId";

    public static final String TRACE_ID_KEY = "traceId";

    public static final String SPAN_ID_KEY = "spanId";

    public static final String PARENT_SPAN_ID_KEY = "parentSpanId";
}
