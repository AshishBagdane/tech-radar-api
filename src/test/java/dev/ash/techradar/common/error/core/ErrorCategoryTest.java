package dev.ash.techradar.common.error.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorCategoryTest {

    @Test
    @DisplayName("Should have correct number of error categories")
    void shouldHaveCorrectNumberOfCategories() {
        assertEquals(4, ErrorCategory.values().length,
                     "ErrorCategory should have exactly 4 categories");
    }

    @Test
    @DisplayName("Should have correct category codes")
    void shouldHaveCorrectCategoryCodes() {
        assertEquals("VAL", ErrorCategory.VALIDATION.getCode());
        assertEquals("RES", ErrorCategory.RESOURCE.getCode());
        assertEquals("OPR", ErrorCategory.OPERATION.getCode());
        assertEquals("SYS", ErrorCategory.SYSTEM.getCode());
    }

    @Test
    @DisplayName("Should have correct category descriptions")
    void shouldHaveCorrectCategoryDescriptions() {
        assertEquals("Validation Error", ErrorCategory.VALIDATION.getDescription());
        assertEquals("Resource Error", ErrorCategory.RESOURCE.getDescription());
        assertEquals("Operation Error", ErrorCategory.OPERATION.getDescription());
        assertEquals("System Error", ErrorCategory.SYSTEM.getDescription());
    }
}
