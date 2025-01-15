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


class RingTest {

    @Test
    void shouldHaveCorrectNumberOfValues() {
        assertEquals(4, Ring.values().length, "Ring should have exactly 4 values");
    }

    @ParameterizedTest
    @EnumSource(Ring.class)
    void shouldHaveNonNullValues(Ring ring) {
        assertNotNull(ring.name(), "Ring name should not be null");
    }

    @Test
    void shouldHaveCorrectEnumValues() {
        assertAll(
            () -> assertTrue(containsEnumValue(Ring.class, "ADOPT")),
            () -> assertTrue(containsEnumValue(Ring.class, "TRIAL")),
            () -> assertTrue(containsEnumValue(Ring.class, "ASSESS")),
            () -> assertTrue(containsEnumValue(Ring.class, "HOLD"))
        );
    }

    @Test
    void shouldCorrectlyParseFromString() {
        assertEquals(Ring.ADOPT, Ring.valueOf("ADOPT"));
        assertEquals(Ring.TRIAL, Ring.valueOf("TRIAL"));
        assertEquals(Ring.ASSESS, Ring.valueOf("ASSESS"));
        assertEquals(Ring.HOLD, Ring.valueOf("HOLD"));
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class,
                     () -> Ring.valueOf("INVALID_RING"));
    }

    @Test
    void shouldMaintainOrdinalOrder() {
        assertTrue(Ring.ADOPT.ordinal() < Ring.TRIAL.ordinal(),
                   "ADOPT should come before TRIAL");
        assertTrue(Ring.TRIAL.ordinal() < Ring.ASSESS.ordinal(),
                   "TRIAL should come before ASSESS");
        assertTrue(Ring.ASSESS.ordinal() < Ring.HOLD.ordinal(),
                   "ASSESS should come before HOLD");
    }
}
