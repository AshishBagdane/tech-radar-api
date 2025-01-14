package dev.ash.techradar.common.error.exception.system;

import dev.ash.techradar.common.error.core.ErrorCode;
import dev.ash.techradar.common.error.core.ErrorContext;
import dev.ash.techradar.common.error.exception.base.AbstractTechRadarException;

/**
 * Base class for system-level exceptions in the Tech Radar application.
 */
public abstract class SystemException extends AbstractTechRadarException {

    protected SystemException(ErrorCode errorCode, ErrorContext errorContext) {
        super(errorCode, errorContext);
    }

    protected SystemException(ErrorCode errorCode, String message, ErrorContext errorContext) {
        super(errorCode, message, errorContext);
    }

    protected SystemException(ErrorCode errorCode, Throwable cause, ErrorContext errorContext) {
        super(errorCode, cause, errorContext);
    }
}
