package dev.ash.techradar.common.observability.metrics.constants;

public final class MetricsConstants {

    private MetricsConstants() {}

    public static final String PREFIX = "techradar.";

    public static final class Tags {

        private Tags() {}

        public static final String QUADRANT = "quadrant";

        public static final String RING = "ring";

        public static final String ENDPOINT = "endpoint";

        public static final String METHOD = "method";

        public static final String STATUS = "status";

        public static final String ERROR_TYPE = "error_type";
    }

    public static final class Metrics {

        private Metrics() {}

        public static final String TECHNOLOGY_CREATED = PREFIX + "technology.created";

        public static final String TECHNOLOGY_UPDATED = PREFIX + "technology.updated";

        public static final String API_LATENCY = PREFIX + "api.latency";

        public static final String ERROR_COUNT = PREFIX + "errors";
    }
}
