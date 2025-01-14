package dev.ash.techradar.common.error.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Comprehensive error codes for the Tech Radar application.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    // Validation Errors (400-419)
    INVALID_INPUT(ErrorCategory.VALIDATION, "TR-400", HttpStatus.BAD_REQUEST, "Invalid input provided"),
    INVALID_FORMAT(ErrorCategory.VALIDATION, "TR-401", HttpStatus.BAD_REQUEST, "Invalid data format"),
    BUSINESS_RULE_VIOLATION(ErrorCategory.VALIDATION, "TR-402", HttpStatus.BAD_REQUEST, "Business rule violation"),

    // Resource Errors (420-439)
    RESOURCE_NOT_FOUND(ErrorCategory.RESOURCE, "TR-404", HttpStatus.NOT_FOUND, "Resource not found"),
    RESOURCE_ALREADY_EXISTS(ErrorCategory.RESOURCE, "TR-409", HttpStatus.CONFLICT, "Resource already exists"),
    RESOURCE_STATE_CONFLICT(ErrorCategory.RESOURCE, "TR-410", HttpStatus.CONFLICT, "Resource state conflict"),

    // Operation Errors (440-459)
    PROCESSING_FAILED(ErrorCategory.OPERATION, "TR-422", HttpStatus.UNPROCESSABLE_ENTITY, "Processing failed"),
    BATCH_OPERATION_FAILED(ErrorCategory.OPERATION, "TR-423", HttpStatus.UNPROCESSABLE_ENTITY,
                           "Batch operation partially failed"),
    INVALID_STATE_TRANSITION(ErrorCategory.OPERATION, "TR-424", HttpStatus.UNPROCESSABLE_ENTITY,
                             "Invalid state transition"),

    // System Errors (500-519)
    INTERNAL_ERROR(ErrorCategory.SYSTEM, "TR-500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    SERVICE_UNAVAILABLE(ErrorCategory.SYSTEM, "TR-503", HttpStatus.SERVICE_UNAVAILABLE,
                        "Service temporarily unavailable"),
    DATABASE_ERROR(ErrorCategory.SYSTEM, "TR-504", HttpStatus.INTERNAL_SERVER_ERROR, "Database operation failed");

    private final ErrorCategory category;

    private final String code;

    private final HttpStatus httpStatus;

    private final String defaultMessage;
}
