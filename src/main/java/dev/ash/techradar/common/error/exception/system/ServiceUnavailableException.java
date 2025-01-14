package dev.ash.techradar.common.error.exception.system;

import dev.ash.techradar.common.error.core.ErrorCode;

import java.time.LocalDateTime;

/**
 * Exception thrown when a system service is temporarily unavailable.
 */
public class ServiceUnavailableException extends SystemException {

    public ServiceUnavailableException(String serviceName, String reason) {
        super(
            ErrorCode.SERVICE_UNAVAILABLE,
            String.format("Service '%s' is currently unavailable: %s", serviceName, reason),
            createErrorContext()
                .attribute("serviceName", serviceName)
                .attribute("reason", reason)
                .attribute("timestamp", LocalDateTime.now())
                .build()
        );
    }

    public ServiceUnavailableException(String serviceName, String reason, LocalDateTime retryAfter) {
        super(
            ErrorCode.SERVICE_UNAVAILABLE,
            String.format("Service '%s' is temporarily unavailable: %s. Please retry after %s",
                          serviceName, reason, retryAfter),
            createErrorContext()
                .attribute("serviceName", serviceName)
                .attribute("reason", reason)
                .attribute("retryAfter", retryAfter)
                .build()
        );
    }
}
