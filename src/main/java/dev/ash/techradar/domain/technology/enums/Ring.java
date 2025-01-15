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
public enum Ring {
    ADOPT("Production ready"),
    TRIAL("Evaluation in progress"),
    ASSESS("Under assessment"),
    HOLD("Not recommended");

    private final String description;

    private static final Map<Ring, Set<Ring>> ALLOWED_TRANSITIONS = new EnumMap<>(Ring.class);

    static {
        // Initialize the transitions after all enum constants are defined
        ALLOWED_TRANSITIONS.put(ADOPT, Collections.unmodifiableSet(EnumSet.of(TRIAL)));
        ALLOWED_TRANSITIONS.put(TRIAL, Collections.unmodifiableSet(EnumSet.of(ADOPT, ASSESS)));
        ALLOWED_TRANSITIONS.put(ASSESS, Collections.unmodifiableSet(EnumSet.of(TRIAL, HOLD)));
        ALLOWED_TRANSITIONS.put(HOLD, Collections.unmodifiableSet(EnumSet.of(ASSESS)));
    }

    public Set<Ring> getAllowedTransitions() {
        return ALLOWED_TRANSITIONS.get(this);
    }

    public boolean canTransitionTo(Ring targetRing) {
        Set<Ring> allowed = ALLOWED_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(targetRing);
    }

    public static Ring getDefaultRing() {
        return ASSESS;
    }
}
