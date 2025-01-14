package dev.ash.techradar.common.error.exception.operation;

import dev.ash.techradar.common.error.core.ErrorCode;
import dev.ash.techradar.common.error.core.ErrorContext;
import dev.ash.techradar.common.error.exception.base.AbstractTechRadarException;

/**
 * Base class for operation-related exceptions in the Tech Radar application.
 */
public abstract class OperationException extends AbstractTechRadarException {

    protected OperationException(ErrorCode errorCode, ErrorContext errorContext) {
        super(errorCode, errorContext);
    }

    protected OperationException(ErrorCode errorCode, String message, ErrorContext errorContext) {
        super(errorCode, message, errorContext);
    }

    protected OperationException(ErrorCode errorCode, Throwable cause, ErrorContext errorContext) {
        super(errorCode, cause, errorContext);
    }
}
