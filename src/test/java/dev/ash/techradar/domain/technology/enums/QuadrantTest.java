package dev.ash.techradar.domain.technology.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static dev.ash.techradar.domain.technology.enums.EnumTestUtils.containsEnumValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuadrantTest {

    @Test
    void shouldHaveCorrectNumberOfValues() {
        assertEquals(4, Quadrant.values().length, "Quadrant should have exactly 4 values");
    }

    @ParameterizedTest
    @EnumSource(Quadrant.class)
    void shouldHaveNonNullValues(Quadrant quadrant) {
        assertNotNull(quadrant.name(), "Quadrant name should not be null");
    }

    @Test
    void shouldHaveCorrectEnumValues() {
        assertAll(
            () -> assertTrue(containsEnumValue(Quadrant.class, "TECHNIQUES")),
            () -> assertTrue(containsEnumValue(Quadrant.class, "TOOLS")),
            () -> assertTrue(containsEnumValue(Quadrant.class, "PLATFORMS")),
            () -> assertTrue(containsEnumValue(Quadrant.class, "LANGUAGES_AND_FRAMEWORKS"))
        );
    }

    @Test
    void shouldCorrectlyParseFromString() {
        assertEquals(Quadrant.TECHNIQUES, Quadrant.valueOf("TECHNIQUES"));
        assertEquals(Quadrant.TOOLS, Quadrant.valueOf("TOOLS"));
        assertEquals(Quadrant.PLATFORMS, Quadrant.valueOf("PLATFORMS"));
        assertEquals(Quadrant.LANGUAGES_AND_FRAMEWORKS, Quadrant.valueOf("LANGUAGES_AND_FRAMEWORKS"));
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class,
                     () -> Quadrant.valueOf("INVALID_QUADRANT"));
    }
}
