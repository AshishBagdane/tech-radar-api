package dev.ash.techradar.common.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TechnologyAccessEvent {

    private UUID technologyId;

    private String accessType;

    private String username;

    private LocalDateTime accessTime;
}
