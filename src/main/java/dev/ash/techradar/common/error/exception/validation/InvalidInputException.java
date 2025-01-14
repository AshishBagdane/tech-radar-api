package dev.ash.techradar.common.error.exception.validation;

import dev.ash.techradar.common.error.core.ErrorCode;

import java.util.Map;
import java.util.Set;

/**
 * Exception thrown when input validation fails.
 */
public class InvalidInputException extends ValidationException {

    public InvalidInputException(String field, String value, String reason) {
        super(
            ErrorCode.INVALID_INPUT,
            String.format("Invalid value '%s' for field '%s': %s", value, field, reason),
            createErrorContext()
                .attribute("field", field)
                .attribute("invalidValue", value)
                .attribute("reason", reason)
                .build()
        );
    }

    public InvalidInputException(Map<String, String> validationErrors) {
        super(
            ErrorCode.INVALID_INPUT,
            "Multiple validation errors occurred",
            createErrorContext()
                .attribute("validationErrors", validationErrors)
                .attribute("errorCount", validationErrors.size())
                .build()
        );
    }

    public InvalidInputException(String field, Set<String> allowedValues, String actualValue) {
        super(
            ErrorCode.INVALID_INPUT,
            String.format("Invalid value '%s' for field '%s'. Allowed values are: %s",
                          actualValue, field, String.join(", ", allowedValues)),
            createErrorContext()
                .attribute("field", field)
                .attribute("invalidValue", actualValue)
                .attribute("allowedValues", allowedValues)
                .build()
        );
    }
}
