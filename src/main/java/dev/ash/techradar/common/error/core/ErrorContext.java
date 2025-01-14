package dev.ash.techradar.common.error.core;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds contextual information about an error.
 */
@Getter
public class ErrorContext {

    private final Map<String, Object> attributes;

    private final String traceId;

    private final String path;

    private final Map<String, String> metadata;

    private ErrorContext(Builder builder) {
        this.attributes = builder.attributes;
        this.traceId = builder.traceId;
        this.path = builder.path;
        this.metadata = builder.metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<String, Object> attributes = new HashMap<>();

        private String traceId;

        private String path;

        private final Map<String, String> metadata = new HashMap<>();

        public Builder attribute(String key, Object value) {
            this.attributes.put(key, value);
            return this;
        }

        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder metadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public ErrorContext build() {
            return new ErrorContext(this);
        }
    }
}
