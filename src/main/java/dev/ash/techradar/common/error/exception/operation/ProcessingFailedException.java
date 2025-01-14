package dev.ash.techradar.common.error.exception.operation;

import dev.ash.techradar.common.error.core.ErrorCode;

/**
 * Exception thrown when a processing operation fails.
 */
public class ProcessingFailedException extends OperationException {

    public ProcessingFailedException(String operation, String reason) {
        super(
            ErrorCode.PROCESSING_FAILED,
            String.format("Failed to process operation '%s': %s", operation, reason),
            createErrorContext()
                .attribute("operation", operation)
                .attribute("reason", reason)
                .build()
        );
    }

    public ProcessingFailedException(String operation, Throwable cause) {
        super(
            ErrorCode.PROCESSING_FAILED,
            cause,
            createErrorContext()
                .attribute("operation", operation)
                .attribute("errorType", cause.getClass().getSimpleName())
                .build()
        );
    }
}
