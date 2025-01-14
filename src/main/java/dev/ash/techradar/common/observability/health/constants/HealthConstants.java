package dev.ash.techradar.common.observability.health.constants;

public final class HealthConstants {

    private HealthConstants() {}

    // Component Names
    public static final String DATABASE_COMPONENT = "database";

    public static final String APPLICATION_COMPONENT = "application";

    public static final String MEMORY_COMPONENT = "memory";

    public static final String TECHNOLOGY_DATA_COMPONENT = "technologyData";

    // Status Messages
    public static final String STATUS_UP = "UP";

    public static final String STATUS_DOWN = "DOWN";

    public static final String STATUS_DEGRADED = "DEGRADED";

    // Thresholds
    public static final double MEMORY_WARN_THRESHOLD = 80.0; // 80% memory usage

    public static final double MEMORY_CRITICAL_THRESHOLD = 90.0; // 90% memory usage

    public static final int MIN_TECHNOLOGIES_COUNT = 1; // Minimum number of technologies expected

    // Detail Keys
    public static final String STATUS_KEY = "status";

    public static final String ERROR_KEY = "error";

    public static final String DETAILS_KEY = "details";

    public static final String USAGE_KEY = "usage";

    public static final String TOTAL_KEY = "total";

    public static final String USED_KEY = "used";

    public static final String FREE_KEY = "free";

    public static final String PERCENTAGE_KEY = "percentage";
}
