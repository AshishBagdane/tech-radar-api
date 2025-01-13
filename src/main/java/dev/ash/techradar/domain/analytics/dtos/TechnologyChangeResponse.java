package dev.ash.techradar.domain.analytics.dtos;

import dev.ash.techradar.common.enums.ChangeType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class TechnologyChangeResponse {

  private UUID technologyId;
  private String name;
  private ChangeType changeType; // ADDED, UPDATED, DELETED
  private LocalDateTime changeDate;
  private String changedBy; // If we add user tracking
}