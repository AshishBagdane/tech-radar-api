package dev.ash.techradar.domain.technology.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Quadrant {
    TECHNIQUES("Development and operational techniques"),
    TOOLS("Development and testing tools"),
    PLATFORMS("Deployment and hosting platforms"),
    LANGUAGES_AND_FRAMEWORKS("Programming languages and frameworks");

    private final String description;

    private static final Map<Quadrant, Boolean> INTERNAL_TRANSITION_FLAGS = new EnumMap<>(Quadrant.class);

    private static final Map<Quadrant, Set<Quadrant>> ALLOWED_TRANSITIONS = new EnumMap<>(Quadrant.class);

    static {
        // Initialize transition flags
        INTERNAL_TRANSITION_FLAGS.put(TECHNIQUES, true);
        INTERNAL_TRANSITION_FLAGS.put(TOOLS, true);
        INTERNAL_TRANSITION_FLAGS.put(PLATFORMS, false);
        INTERNAL_TRANSITION_FLAGS.put(LANGUAGES_AND_FRAMEWORKS, false);

        // Initialize allowed transitions
        for (Quadrant quadrant : values()) {
            if (INTERNAL_TRANSITION_FLAGS.get(quadrant)) {
                Set<Quadrant> transitions = EnumSet.noneOf(Quadrant.class);
                for (Quadrant target : values()) {
                    if (target != quadrant && INTERNAL_TRANSITION_FLAGS.get(target)) {
                        transitions.add(target);
                    }
                }
                ALLOWED_TRANSITIONS.put(quadrant, Collections.unmodifiableSet(transitions));
            } else {
                ALLOWED_TRANSITIONS.put(quadrant, Collections.emptySet());
            }
        }
    }

    public boolean allowsInternalTransition() {
        return INTERNAL_TRANSITION_FLAGS.get(this);
    }

    public boolean canTransitionTo(Quadrant targetQuadrant) {
        return ALLOWED_TRANSITIONS.get(this).contains(targetQuadrant);
    }

    public Set<Quadrant> getAllowedTransitions() {
        return ALLOWED_TRANSITIONS.get(this);
    }

    public static Quadrant getDefaultQuadrant() {
        return LANGUAGES_AND_FRAMEWORKS;
    }
}
