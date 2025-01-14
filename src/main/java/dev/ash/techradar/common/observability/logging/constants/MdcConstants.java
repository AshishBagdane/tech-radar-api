package dev.ash.techradar.common.observability.logging.constants;

public final class MdcConstants {

    private MdcConstants() {}

    // Request context
    public static final String CORRELATION_ID = "correlationId";

    public static final String REQUEST_ID = "requestId";

    public static final String SESSION_ID = "sessionId";

    public static final String REQUEST_PATH = "requestPath";

    public static final String REQUEST_METHOD = "requestMethod";

    // Business context
    public static final String QUADRANT = "quadrant";

    public static final String RING = "ring";

    public static final String TECHNOLOGY_ID = "technologyId";

    // Performance context
    public static final String EXECUTION_TIME = "executionTime";

    // Error context
    public static final String ERROR_CODE = "errorCode";

    public static final String ERROR_TYPE = "errorType";

    public static final String STACK_TRACE = "stackTrace";
}
