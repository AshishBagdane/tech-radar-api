package dev.ash.techradar.domain.technology.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tech Radar Enum Tests")
class TransitionStateTest {

    @Nested
    @DisplayName("Ring Transition Tests")
    class RingTransitionTest {

        @Test
        @DisplayName("ADOPT should only transition to TRIAL")
        void adoptTransitions() {
            Set<Ring> transitions = Ring.ADOPT.getAllowedTransitions();
            assertAll(
                () -> assertEquals(1, transitions.size(), "ADOPT should have exactly one transition"),
                () -> assertTrue(transitions.contains(Ring.TRIAL), "ADOPT should transition to TRIAL"),
                () -> assertTrue(Ring.ADOPT.canTransitionTo(Ring.TRIAL), "ADOPT should be able to transition to TRIAL"),
                () -> assertFalse(Ring.ADOPT.canTransitionTo(Ring.ASSESS), "ADOPT should not transition to ASSESS"),
                () -> assertFalse(Ring.ADOPT.canTransitionTo(Ring.HOLD), "ADOPT should not transition to HOLD")
            );
        }

        @Test
        @DisplayName("TRIAL should transition to ADOPT or ASSESS")
        void trialTransitions() {
            Set<Ring> transitions = Ring.TRIAL.getAllowedTransitions();
            assertAll(
                () -> assertEquals(2, transitions.size(), "TRIAL should have exactly two transitions"),
                () -> assertTrue(transitions.contains(Ring.ADOPT), "TRIAL should transition to ADOPT"),
                () -> assertTrue(transitions.contains(Ring.ASSESS), "TRIAL should transition to ASSESS"),
                () -> assertFalse(transitions.contains(Ring.HOLD), "TRIAL should not transition to HOLD")
            );
        }

        @Test
        @DisplayName("ASSESS should transition to TRIAL or HOLD")
        void assessTransitions() {
            Set<Ring> transitions = Ring.ASSESS.getAllowedTransitions();
            assertAll(
                () -> assertEquals(2, transitions.size(), "ASSESS should have exactly two transitions"),
                () -> assertTrue(transitions.contains(Ring.TRIAL), "ASSESS should transition to TRIAL"),
                () -> assertTrue(transitions.contains(Ring.HOLD), "ASSESS should transition to HOLD"),
                () -> assertFalse(transitions.contains(Ring.ADOPT), "ASSESS should not transition to ADOPT")
            );
        }

        @Test
        @DisplayName("HOLD should only transition to ASSESS")
        void holdTransitions() {
            Set<Ring> transitions = Ring.HOLD.getAllowedTransitions();
            assertAll(
                () -> assertEquals(1, transitions.size(), "HOLD should have exactly one transition"),
                () -> assertTrue(transitions.contains(Ring.ASSESS), "HOLD should transition to ASSESS"),
                () -> assertFalse(transitions.contains(Ring.TRIAL), "HOLD should not transition to TRIAL"),
                () -> assertFalse(transitions.contains(Ring.ADOPT), "HOLD should not transition to ADOPT")
            );
        }

        @ParameterizedTest
        @EnumSource(Ring.class)
        @DisplayName("No Ring should transition to itself")
        void noSelfTransitions(Ring ring) {
            assertFalse(ring.canTransitionTo(ring),
                        String.format("%s should not be able to transition to itself", ring));
        }

        @Test
        @DisplayName("Transition sets should be immutable")
        void transitionSetsAreImmutable() {
            for (Ring ring : Ring.values()) {
                Set<Ring> transitions = ring.getAllowedTransitions();
                assertThrows(UnsupportedOperationException.class,
                             () -> transitions.add(Ring.ADOPT),
                             "Transition set should be immutable");
            }
        }
    }

    @Nested
    @DisplayName("Quadrant Transition Tests")
    class QuadrantTransitionTest {

        @Test
        @DisplayName("Verify internal transition flags")
        void verifyInternalTransitionFlags() {
            assertAll(
                () -> assertTrue(Quadrant.TECHNIQUES.allowsInternalTransition(),
                                 "TECHNIQUES should allow internal transitions"),
                () -> assertTrue(Quadrant.TOOLS.allowsInternalTransition(),
                                 "TOOLS should allow internal transitions"),
                () -> assertFalse(Quadrant.PLATFORMS.allowsInternalTransition(),
                                  "PLATFORMS should not allow internal transitions"),
                () -> assertFalse(Quadrant.LANGUAGES_AND_FRAMEWORKS.allowsInternalTransition(),
                                  "LANGUAGES_AND_FRAMEWORKS should not allow internal transitions")
            );
        }

        @ParameterizedTest
        @MethodSource("quadrantTransitionProvider")
        @DisplayName("Verify allowed transitions between quadrants")
        void verifyAllowedTransitions(Quadrant from, Quadrant to, boolean expected) {
            assertEquals(expected, from.canTransitionTo(to),
                         String.format("Transition from %s to %s should be %s",
                                       from, to, expected ? "allowed" : "disallowed"));
        }

        private static Stream<Arguments> quadrantTransitionProvider() {
            return Stream.of(
                Arguments.of(Quadrant.TECHNIQUES, Quadrant.TOOLS, true),
                Arguments.of(Quadrant.TOOLS, Quadrant.TECHNIQUES, true),
                Arguments.of(Quadrant.TECHNIQUES, Quadrant.PLATFORMS, false),
                Arguments.of(Quadrant.TOOLS, Quadrant.LANGUAGES_AND_FRAMEWORKS, false),
                Arguments.of(Quadrant.PLATFORMS, Quadrant.LANGUAGES_AND_FRAMEWORKS, false),
                Arguments.of(Quadrant.LANGUAGES_AND_FRAMEWORKS, Quadrant.PLATFORMS, false)
            );
        }

        @Test
        @DisplayName("Verify transition sets for non-transitional quadrants")
        void verifyNonTransitionalQuadrants() {
            assertAll(
                () -> assertTrue(Quadrant.PLATFORMS.getAllowedTransitions().isEmpty(),
                                 "PLATFORMS should have no allowed transitions"),
                () -> assertTrue(Quadrant.LANGUAGES_AND_FRAMEWORKS.getAllowedTransitions().isEmpty(),
                                 "LANGUAGES_AND_FRAMEWORKS should have no allowed transitions")
            );
        }

        @Test
        @DisplayName("Verify transition sets for transitional quadrants")
        void verifyTransitionalQuadrants() {
            assertAll(
                () -> assertEquals(1, Quadrant.TECHNIQUES.getAllowedTransitions().size(),
                                   "TECHNIQUES should have exactly one transition"),
                () -> assertTrue(Quadrant.TECHNIQUES.getAllowedTransitions().contains(Quadrant.TOOLS),
                                 "TECHNIQUES should transition to TOOLS"),
                () -> assertEquals(1, Quadrant.TOOLS.getAllowedTransitions().size(),
                                   "TOOLS should have exactly one transition"),
                () -> assertTrue(Quadrant.TOOLS.getAllowedTransitions().contains(Quadrant.TECHNIQUES),
                                 "TOOLS should transition to TECHNIQUES")
            );
        }

        @ParameterizedTest
        @EnumSource(Quadrant.class)
        @DisplayName("No Quadrant should transition to itself")
        void noSelfTransitions(Quadrant quadrant) {
            assertFalse(quadrant.canTransitionTo(quadrant),
                        String.format("%s should not be able to transition to itself", quadrant));
        }

        @Test
        @DisplayName("Transition sets should be immutable")
        void transitionSetsAreImmutable() {
            for (Quadrant quadrant : Quadrant.values()) {
                Set<Quadrant> transitions = quadrant.getAllowedTransitions();
                assertThrows(UnsupportedOperationException.class,
                             () -> transitions.add(Quadrant.TOOLS),
                             "Transition set should be immutable");
            }
        }
    }
}
