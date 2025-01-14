package dev.ash.techradar.common.error.logging;

import dev.ash.techradar.common.error.core.ErrorMessage;
import dev.ash.techradar.common.error.exception.base.AbstractTechRadarException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Centralized error logging component for the Tech Radar application.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorLogger {

    private static final String ERROR_CODE_MDC_KEY = "errorCode";

    private static final String ERROR_TYPE_MDC_KEY = "errorType";

    private static final String TRACE_ID_MDC_KEY = "traceId";

    private static final String REQUEST_PATH_MDC_KEY = "requestPath";

    private final LoggingUtils loggingUtils;

    /**
     * Logs a TechRadar exception with context.
     */
    public void logException(AbstractTechRadarException exception) {
        ErrorMessage errorMessage = exception.getErrorMessage();

        try {
            setupMDC(errorMessage, exception);
            log.error("TechRadar error occurred: {} - {}",
                      exception.getErrorCode(),
                      errorMessage.getMessage(),
                      maskSensitiveData(errorMessage));
        } finally {
            clearMDC();
        }
    }

    /**
     * Logs an unexpected exception.
     */
    public void logUnexpectedException(Throwable exception, String context) {
        try {
            MDC.put(ERROR_TYPE_MDC_KEY, exception.getClass().getSimpleName());
            MDC.put(TRACE_ID_MDC_KEY, loggingUtils.getCurrentTraceId());

            log.error("Unexpected error in {}: {}",
                      context,
                      exception.getMessage(),
                      exception);
        } finally {
            clearMDC();
        }
    }

    /**
     * Logs validation errors.
     */
    public void logValidationErrors(Map<String, String> validationErrors, String path) {
        try {
            MDC.put(ERROR_TYPE_MDC_KEY, "ValidationError");
            MDC.put(REQUEST_PATH_MDC_KEY, path);
            MDC.put(TRACE_ID_MDC_KEY, loggingUtils.getCurrentTraceId());

            log.error("Validation failed with {} error(s): {}",
                      validationErrors.size(),
                      maskSensitiveData(validationErrors));
        } finally {
            clearMDC();
        }
    }

    /**
     * Sets up MDC context for logging.
     */
    private void setupMDC(ErrorMessage errorMessage, AbstractTechRadarException exception) {
        MDC.put(ERROR_CODE_MDC_KEY, exception.getErrorCode().getCode());
        MDC.put(ERROR_TYPE_MDC_KEY, exception.getClass().getSimpleName());
        MDC.put(TRACE_ID_MDC_KEY,
                Optional.ofNullable(errorMessage.getTraceId())
                    .orElseGet(loggingUtils::getCurrentTraceId));

        if (errorMessage.getPath() != null) {
            MDC.put(REQUEST_PATH_MDC_KEY, errorMessage.getPath());
        }
    }

    /**
     * Clears MDC context.
     */
    private void clearMDC() {
        MDC.remove(ERROR_CODE_MDC_KEY);
        MDC.remove(ERROR_TYPE_MDC_KEY);
        MDC.remove(TRACE_ID_MDC_KEY);
        MDC.remove(REQUEST_PATH_MDC_KEY);
    }

    /**
     * Masks sensitive data in error messages.
     */
    private Object maskSensitiveData(Object data) {
        return loggingUtils.maskSensitiveData(data);
    }
}
