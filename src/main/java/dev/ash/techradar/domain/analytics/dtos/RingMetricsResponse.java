package dev.ash.techradar.domain.analytics.dtos;

import dev.ash.techradar.domain.technology.enums.Ring;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class RingMetricsResponse {

  private Map<Ring, Integer> distributionByRing;
  private int totalTechnologies;
  private LocalDateTime lastUpdated;
}