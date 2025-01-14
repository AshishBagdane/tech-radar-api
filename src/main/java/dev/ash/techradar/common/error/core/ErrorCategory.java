package dev.ash.techradar.common.error.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines the main categories of errors in the Tech Radar application.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCategory {
    VALIDATION("VAL", "Validation Error"),
    RESOURCE("RES", "Resource Error"),
    OPERATION("OPR", "Operation Error"),
    SYSTEM("SYS", "System Error");

    private final String code;

    private final String description;
}
