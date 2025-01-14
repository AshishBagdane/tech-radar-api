package dev.ash.techradar.common.error.exception.validation;

import dev.ash.techradar.common.error.core.ErrorCode;
import dev.ash.techradar.common.error.core.ErrorContext;
import dev.ash.techradar.common.error.exception.base.AbstractTechRadarException;

/**
 * Base class for validation-related exceptions in the Tech Radar application.
 */
public abstract class ValidationException extends AbstractTechRadarException {

    protected ValidationException(ErrorCode errorCode, ErrorContext errorContext) {
        super(errorCode, errorContext);
    }

    protected ValidationException(ErrorCode errorCode, String message, ErrorContext errorContext) {
        super(errorCode, message, errorContext);
    }

    protected ValidationException(ErrorCode errorCode, Throwable cause, ErrorContext errorContext) {
        super(errorCode, cause, errorContext);
    }
}
