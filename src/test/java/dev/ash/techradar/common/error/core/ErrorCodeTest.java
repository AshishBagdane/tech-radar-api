package dev.ash.techradar.common.error.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorCodeTest {

    @Test
    @DisplayName("Should have correct number of error codes")
    void shouldHaveCorrectNumberOfErrorCodes() {
        assertEquals(12, ErrorCode.values().length,
                     "ErrorCode should have exactly 12 error codes");
    }

    @Test
    @DisplayName("Should maintain consistent code format")
    void shouldMaintainConsistentCodeFormat() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            assertTrue(errorCode.getCode().startsWith("TR-"),
                       "Error code should start with 'TR-'");
            assertTrue(errorCode.getCode().matches("TR-\\d{3}"),
                       "Error code should follow format TR-XXX where X is a digit");
        }
    }

    @Test
    @DisplayName("Should have appropriate HTTP status codes")
    void shouldHaveAppropriateHttpStatusCodes() {
        // Validation errors should be 400
        assertEquals(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT.getHttpStatus());
        assertEquals(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_FORMAT.getHttpStatus());
        assertEquals(HttpStatus.BAD_REQUEST, ErrorCode.BUSINESS_RULE_VIOLATION.getHttpStatus());

        // Resource errors should be 404 or 409
        assertEquals(HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus());
        assertEquals(HttpStatus.CONFLICT, ErrorCode.RESOURCE_ALREADY_EXISTS.getHttpStatus());
        assertEquals(HttpStatus.CONFLICT, ErrorCode.RESOURCE_STATE_CONFLICT.getHttpStatus());

        // Operation errors should be 422
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.PROCESSING_FAILED.getHttpStatus());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.BATCH_OPERATION_FAILED.getHttpStatus());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_STATE_TRANSITION.getHttpStatus());

        // System errors should be 500 or 503
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR.getHttpStatus());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.SERVICE_UNAVAILABLE.getHttpStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.DATABASE_ERROR.getHttpStatus());
    }

    @Test
    @DisplayName("Should have correct error categories assigned")
    void shouldHaveCorrectErrorCategories() {
        // Check validation category
        assertSame(ErrorCategory.VALIDATION, ErrorCode.INVALID_INPUT.getCategory());
        assertSame(ErrorCategory.VALIDATION, ErrorCode.INVALID_FORMAT.getCategory());
        assertSame(ErrorCategory.VALIDATION, ErrorCode.BUSINESS_RULE_VIOLATION.getCategory());

        // Check resource category
        assertSame(ErrorCategory.RESOURCE, ErrorCode.RESOURCE_NOT_FOUND.getCategory());
        assertSame(ErrorCategory.RESOURCE, ErrorCode.RESOURCE_ALREADY_EXISTS.getCategory());
        assertSame(ErrorCategory.RESOURCE, ErrorCode.RESOURCE_STATE_CONFLICT.getCategory());

        // Check operation category
        assertSame(ErrorCategory.OPERATION, ErrorCode.PROCESSING_FAILED.getCategory());
        assertSame(ErrorCategory.OPERATION, ErrorCode.BATCH_OPERATION_FAILED.getCategory());
        assertSame(ErrorCategory.OPERATION, ErrorCode.INVALID_STATE_TRANSITION.getCategory());

        // Check system category
        assertSame(ErrorCategory.SYSTEM, ErrorCode.INTERNAL_ERROR.getCategory());
        assertSame(ErrorCategory.SYSTEM, ErrorCode.SERVICE_UNAVAILABLE.getCategory());
        assertSame(ErrorCategory.SYSTEM, ErrorCode.DATABASE_ERROR.getCategory());
    }

    @Test
    @DisplayName("Should have non-null default messages")
    void shouldHaveNonNullDefaultMessages() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            assertNotNull(errorCode.getDefaultMessage(),
                          "Default message should not be null for " + errorCode.name());
            assertFalse(errorCode.getDefaultMessage().isEmpty(),
                        "Default message should not be empty for " + errorCode.name());
        }
    }

    @Test
    @DisplayName("Should have unique error codes")
    void shouldHaveUniqueErrorCodes() {
        long uniqueCodes = java.util.Arrays.stream(ErrorCode.values())
            .map(ErrorCode::getCode)
            .distinct()
            .count();

        assertEquals(ErrorCode.values().length, uniqueCodes,
                     "All error codes should be unique");
    }
}
