package dev.ash.techradar.domain.analytics.dtos;

import dev.ash.techradar.domain.technology.enums.Quadrant;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class QuadrantMetricsResponse {

    private Map<Quadrant, Integer> distributionByQuadrant;

    private int totalTechnologies;

    private LocalDateTime lastUpdated;
}
