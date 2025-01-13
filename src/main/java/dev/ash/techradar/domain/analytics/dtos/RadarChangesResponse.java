package dev.ash.techradar.domain.analytics.dtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class RadarChangesResponse {

  private List<TechnologyChangeResponse> changes;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private int totalChanges;
}