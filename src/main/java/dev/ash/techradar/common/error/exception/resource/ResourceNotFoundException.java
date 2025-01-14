package dev.ash.techradar.common.error.exception.resource;

import dev.ash.techradar.common.error.core.ErrorCode;

/**
 * Exception thrown when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends ResourceException {

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(
            ErrorCode.RESOURCE_NOT_FOUND,
            String.format("%s with ID '%s' not found", resourceType, resourceId),
            createErrorContext()
                .attribute("resourceType", resourceType)
                .attribute("resourceId", resourceId)
                .build()
        );
    }

    public ResourceNotFoundException(String resourceType, String field, String value) {
        super(
            ErrorCode.RESOURCE_NOT_FOUND,
            String.format("%s with %s '%s' not found", resourceType, field, value),
            createErrorContext()
                .attribute("resourceType", resourceType)
                .attribute("field", field)
                .attribute("value", value)
                .build()
        );
    }
}
