package dev.ash.techradar.domain.analytics.dtos;

import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class RingMetricsResponse {

    private Map<Ring, Integer> distributionByRing;

    private int totalTechnologies;

    private LocalDateTime lastUpdated;
}
