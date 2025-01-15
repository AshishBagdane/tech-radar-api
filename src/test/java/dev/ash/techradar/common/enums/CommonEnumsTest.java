package dev.ash.techradar.common.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonEnumsTest {

    @Nested
    @DisplayName("ChangeType Tests")
    class ChangeTypeTest {

        @Test
        @DisplayName("Should have exactly three values")
        void shouldHaveThreeValues() {
            assertEquals(3, ChangeType.values().length,
                         "ChangeType should have exactly three values");
        }

        @ParameterizedTest
        @EnumSource(ChangeType.class)
        @DisplayName("Values should be correctly named")
        void shouldHaveCorrectNames(ChangeType type) {
            assertTrue(
                Arrays.asList("ADDED", "UPDATED", "DELETED").contains(type.name()),
                "ChangeType should have correct name: " + type.name()
            );
        }

        @Test
        @DisplayName("Should parse all valid values")
        void shouldParseValidValues() {
            assertAll(
                () -> assertEquals(ChangeType.ADDED, ChangeType.valueOf("ADDED")),
                () -> assertEquals(ChangeType.UPDATED, ChangeType.valueOf("UPDATED")),
                () -> assertEquals(ChangeType.DELETED, ChangeType.valueOf("DELETED"))
            );
        }

        @Test
        @DisplayName("Should maintain correct ordinal order")
        void shouldHaveCorrectOrder() {
            assertAll(
                () -> assertTrue(ChangeType.ADDED.ordinal() < ChangeType.UPDATED.ordinal(),
                                 "ADDED should come before UPDATED"),
                () -> assertTrue(ChangeType.UPDATED.ordinal() < ChangeType.DELETED.ordinal(),
                                 "UPDATED should come before DELETED")
            );
        }

        @Test
        @DisplayName("Should throw exception for invalid value")
        void shouldThrowExceptionForInvalidValue() {
            assertThrows(IllegalArgumentException.class,
                         () -> ChangeType.valueOf("INVALID"),
                         "Should throw exception for invalid ChangeType value");
        }

        @ParameterizedTest
        @MethodSource("changeTypeComparisonProvider")
        @DisplayName("Should compare correctly with other values")
        void shouldCompareCorrectly(ChangeType type1, ChangeType type2, boolean expectBefore) {
            if (expectBefore) {
                assertTrue(type1.ordinal() < type2.ordinal(),
                           String.format("%s should come before %s", type1, type2));
            } else {
                assertTrue(type1.ordinal() > type2.ordinal(),
                           String.format("%s should come after %s", type1, type2));
            }
        }

        private static Stream<Arguments> changeTypeComparisonProvider() {
            return Stream.of(
                Arguments.of(ChangeType.ADDED, ChangeType.UPDATED, true),
                Arguments.of(ChangeType.ADDED, ChangeType.DELETED, true),
                Arguments.of(ChangeType.UPDATED, ChangeType.DELETED, true),
                Arguments.of(ChangeType.DELETED, ChangeType.ADDED, false),
                Arguments.of(ChangeType.DELETED, ChangeType.UPDATED, false),
                Arguments.of(ChangeType.UPDATED, ChangeType.ADDED, false)
            );
        }
    }

    @Nested
    @DisplayName("SortDirection Tests")
    class SortDirectionTest {

        @Test
        @DisplayName("Should have exactly two values")
        void shouldHaveTwoValues() {
            assertEquals(2, SortDirection.values().length,
                         "SortDirection should have exactly two values");
        }

        @ParameterizedTest
        @EnumSource(SortDirection.class)
        @DisplayName("Values should be correctly named")
        void shouldHaveCorrectNames(SortDirection direction) {
            assertTrue(
                Arrays.asList("ASC", "DESC").contains(direction.name()),
                "SortDirection should have correct name: " + direction.name()
            );
        }

        @Test
        @DisplayName("Should parse all valid values")
        void shouldParseValidValues() {
            assertAll(
                () -> assertEquals(SortDirection.ASC, SortDirection.valueOf("ASC")),
                () -> assertEquals(SortDirection.DESC, SortDirection.valueOf("DESC"))
            );
        }

        @Test
        @DisplayName("Should maintain correct ordinal order")
        void shouldHaveCorrectOrder() {
            assertTrue(SortDirection.ASC.ordinal() < SortDirection.DESC.ordinal(),
                       "ASC should come before DESC");
        }

        @Test
        @DisplayName("Should throw exception for invalid value")
        void shouldThrowExceptionForInvalidValue() {
            assertThrows(IllegalArgumentException.class,
                         () -> SortDirection.valueOf("INVALID"),
                         "Should throw exception for invalid SortDirection value");
        }

        @Test
        @DisplayName("Values should be opposites")
        void valuesShouldBeOpposites() {
            assertAll(
                () -> assertNotEquals(SortDirection.ASC, SortDirection.DESC,
                                      "ASC and DESC should be different values"),
                () -> assertEquals(0, SortDirection.ASC.ordinal(),
                                   "ASC should be first value"),
                () -> assertEquals(1, SortDirection.DESC.ordinal(),
                                   "DESC should be second value")
            );
        }

        @ParameterizedTest
        @EnumSource(value = SortDirection.class, names = {"ASC"})
        @DisplayName("ASC should be first value")
        void ascShouldBeFirst(SortDirection direction) {
            assertEquals(0, direction.ordinal(),
                         "ASC should have ordinal value of 0");
        }

        @ParameterizedTest
        @EnumSource(value = SortDirection.class, names = {"DESC"})
        @DisplayName("DESC should be second value")
        void descShouldBeSecond(SortDirection direction) {
            assertEquals(1, direction.ordinal(),
                         "DESC should have ordinal value of 1");
        }
    }
}
