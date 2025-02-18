package dev.ash.techradar.domain.technology.events;

import dev.ash.techradar.common.enums.ChangeType;
import dev.ash.techradar.domain.technology.entities.Technology;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class TechnologyChangeEvent {

    private Technology technology;

    private ChangeType changeType;

    private LocalDateTime changeDate;

    private Map<String, PropertyChange> changes;

    private String initiatedBy;  // For audit tracking

    @Data
    @Builder
    public static class PropertyChange {

        private String field;

        private String previousValue;

        private String newValue;
    }
}
