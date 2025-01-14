package dev.ash.techradar.common.error.exception.resource;

import dev.ash.techradar.common.error.core.ErrorCode;

import java.util.Map;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class ResourceAlreadyExistsException extends ResourceException {

    public ResourceAlreadyExistsException(String resourceType, String identifier) {
        super(
            ErrorCode.RESOURCE_ALREADY_EXISTS,
            String.format("%s already exists with this identifier: %s", resourceType, identifier),
            createErrorContext()
                .attribute("resourceType", resourceType)
                .attribute("identifier", identifier)
                .build()
        );
    }

    public ResourceAlreadyExistsException(String resourceType, Map<String, Object> identifiers) {
        super(
            ErrorCode.RESOURCE_ALREADY_EXISTS,
            String.format("%s already exists with provided identifiers", resourceType),
            createErrorContext()
                .attribute("resourceType", resourceType)
                .attribute("identifiers", identifiers)
                .build()
        );
    }
}
