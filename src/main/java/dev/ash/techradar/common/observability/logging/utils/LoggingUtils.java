package dev.ash.techradar.common.observability.logging.utils;

import dev.ash.techradar.common.observability.logging.constants.MdcConstants;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public final class LoggingUtils {

    private LoggingUtils() {}

    public static void withMdc(Map<String, String> contextMap, Runnable operation) {
        Map<String, String> previousContext = MDC.getCopyOfContextMap();
        try {
            contextMap.forEach(MDC::put);
            operation.run();
        } finally {
            restoreMdc(previousContext);
        }
    }

    public static <T> T withMdc(Map<String, String> contextMap, Supplier<T> operation) {
        Map<String, String> previousContext = MDC.getCopyOfContextMap();
        try {
            contextMap.forEach(MDC::put);
            return operation.get();
        } finally {
            restoreMdc(previousContext);
        }
    }

    public static void addRequestContext(String path, String method) {
        MDC.put(MdcConstants.REQUEST_PATH, path);
        MDC.put(MdcConstants.REQUEST_METHOD, method);
        if (MDC.get(MdcConstants.CORRELATION_ID) == null) {
            MDC.put(MdcConstants.CORRELATION_ID, generateCorrelationId());
        }
    }

    public static void addBusinessContext(String quadrant, String ring, String technologyId) {
        Optional.ofNullable(quadrant).ifPresent(q -> MDC.put(MdcConstants.QUADRANT, q));
        Optional.ofNullable(ring).ifPresent(r -> MDC.put(MdcConstants.RING, r));
        Optional.ofNullable(technologyId).ifPresent(t -> MDC.put(MdcConstants.TECHNOLOGY_ID, t));
    }

    public static void addErrorContext(String errorCode, String errorType, Throwable error) {
        MDC.put(MdcConstants.ERROR_CODE, errorCode);
        MDC.put(MdcConstants.ERROR_TYPE, errorType);
        if (error != null) {
            MDC.put(MdcConstants.STACK_TRACE, getStackTraceAsString(error));
        }
    }

    public static void clearMdc() {
        MDC.clear();
    }

    private static void restoreMdc(Map<String, String> previousContext) {
        MDC.clear();
        if (previousContext != null) {
            previousContext.forEach(MDC::put);
        }
    }

    private static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
