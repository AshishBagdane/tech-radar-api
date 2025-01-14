package dev.ash.techradar.common.error.exception.system;

import dev.ash.techradar.common.error.core.ErrorCode;

/**
 * Exception thrown when an unexpected internal error occurs.
 */
public class InternalErrorException extends SystemException {

    public InternalErrorException(String operation, Throwable cause) {
        super(
            ErrorCode.INTERNAL_ERROR,
            cause,
            createErrorContext()
                .attribute("operation", operation)
                .attribute("errorType", cause.getClass().getSimpleName())
                .attribute("errorMessage", cause.getMessage())
                .build()
        );
    }

    public InternalErrorException(String operation, String reason) {
        super(
            ErrorCode.INTERNAL_ERROR,
            String.format("Internal error during '%s': %s", operation, reason),
            createErrorContext()
                .attribute("operation", operation)
                .attribute("reason", reason)
                .build()
        );
    }
}
