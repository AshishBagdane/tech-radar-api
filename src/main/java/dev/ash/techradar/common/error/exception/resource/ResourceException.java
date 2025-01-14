package dev.ash.techradar.common.error.exception.resource;

import dev.ash.techradar.common.error.core.ErrorCode;
import dev.ash.techradar.common.error.core.ErrorContext;
import dev.ash.techradar.common.error.exception.base.AbstractTechRadarException;

/**
 * Base class for resource-related exceptions in the Tech Radar application.
 */
public abstract class ResourceException extends AbstractTechRadarException {

    protected ResourceException(ErrorCode errorCode, ErrorContext errorContext) {
        super(errorCode, errorContext);
    }

    protected ResourceException(ErrorCode errorCode, String message, ErrorContext errorContext) {
        super(errorCode, message, errorContext);
    }

    protected ResourceException(ErrorCode errorCode, Throwable cause, ErrorContext errorContext) {
        super(errorCode, cause, errorContext);
    }
}
