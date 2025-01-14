package dev.ash.techradar.common.error.exception.resource;

import dev.ash.techradar.common.error.core.ErrorCode;

/**
 * Exception thrown when a resource is in an incompatible state for the requested operation.
 */
public class ResourceStateConflictException extends ResourceException {

    public ResourceStateConflictException(String resourceType, String resourceId,
                                          String currentState, String requiredState) {
        super(
            ErrorCode.RESOURCE_STATE_CONFLICT,
            String.format("%s (ID: %s) is in state '%s', but requires state '%s'",
                          resourceType, resourceId, currentState, requiredState),
            createErrorContext()
                .attribute("resourceType", resourceType)
                .attribute("resourceId", resourceId)
                .attribute("currentState", currentState)
                .attribute("requiredState", requiredState)
                .build()
        );
    }

    public ResourceStateConflictException(String resourceType, String resourceId,
                                          String currentState, String operation, String reason) {
        super(
            ErrorCode.RESOURCE_STATE_CONFLICT,
            String.format("Cannot perform '%s' on %s (ID: %s) in state '%s': %s",
                          operation, resourceType, resourceId, currentState, reason),
            createErrorContext()
                .attribute("resourceType", resourceType)
                .attribute("resourceId", resourceId)
                .attribute("currentState", currentState)
                .attribute("operation", operation)
                .attribute("reason", reason)
                .build()
        );
    }
}
