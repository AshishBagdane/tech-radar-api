package dev.ash.techradar.domain.technology.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ash.techradar.domain.technology.entities.Technology;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeTrackingUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object[]> compareEntities(Technology oldEntity, Technology newEntity) {
        Map<String, Object[]> changes = new HashMap<>();

        // Track basic fields
        String[] basicFields = {
            "name", "description", "quadrant", "ring"
        };

        // Compare basic fields
        for (String fieldName : basicFields) {
            try {
                Field field = ReflectionUtils.findField(Technology.class, fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    Object oldValue = field.get(oldEntity);
                    Object newValue = field.get(newEntity);

                    if (!Objects.equals(oldValue, newValue)) {
                        changes.put(fieldName, new Object[]{oldValue, newValue});
                    }
                }
            } catch (IllegalAccessException e) {
                // Log error
            }
        }

        // Compare metadata separately
        Map<String, String> oldMetadata = oldEntity.getMetadata();
        Map<String, String> newMetadata = newEntity.getMetadata();

        // Track added metadata
        newMetadata.forEach((key, value) -> {
            if (!oldMetadata.containsKey(key)) {
                changes.put("metadata.added." + key,
                            new Object[]{null, value});
            } else if (!Objects.equals(oldMetadata.get(key), value)) {
                changes.put("metadata.updated." + key,
                            new Object[]{oldMetadata.get(key), value});
            }
        });

        // Track removed metadata
        oldMetadata.forEach((key, value) -> {
            if (!newMetadata.containsKey(key)) {
                changes.put("metadata.removed." + key,
                            new Object[]{value, null});
            }
        });

        return changes;
    }

    public static String formatMetadataChange(String operation, String key, Object[] values) {
        switch (operation) {
            case "added":
                return String.format("Added metadata '%s' with value '%s'",
                                     key, values[1]);
            case "removed":
                return String.format("Removed metadata '%s' (was '%s')",
                                     key, values[0]);
            case "updated":
                return String.format("Updated metadata '%s' from '%s' to '%s'",
                                     key, values[0], values[1]);
            default:
                return "Unknown metadata change";
        }
    }

    public static String serializeChanges(Map<String, Object[]> changes) {
        try {
            // Transform to a more readable format
            Map<String, ChangeEntry> formattedChanges = new HashMap<>();

            changes.forEach((key, values) -> {
                ChangeEntry entry = new ChangeEntry(
                    values[0] != null ? formatValue(values[0]) : null,
                    values[1] != null ? formatValue(values[1]) : null
                );
                formattedChanges.put(key, entry);
            });

            String json = objectMapper.writeValueAsString(formattedChanges);
            log.debug("Serialized changes: {}", json);

            return objectMapper.writeValueAsString(formattedChanges);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize changes", e);
            return "{}";
        }
    }

    public static Map<String, ChangeEntry> deserializeChanges(String json) {
        try {
            if (json == null || json.isBlank()) {
                return Collections.emptyMap();
            }
            return objectMapper.readValue(json,
                                          new TypeReference<Map<String, ChangeEntry>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize changes", e);
            return Collections.emptyMap();
        }
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        }
        return value.toString();
    }

    // Inner class for change entries
    public static class ChangeEntry {

        private final String previousValue;

        private final String newValue;

        public ChangeEntry(String previousValue, String newValue) {
            this.previousValue = previousValue;
            this.newValue = newValue;
        }

        // Getters
        public String getPreviousValue() {return previousValue;}

        public String getNewValue() {return newValue;}
    }
}
