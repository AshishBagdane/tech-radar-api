package dev.ash.techradar.domain.analytics.services;

import dev.ash.techradar.domain.analytics.dtos.QuadrantMetricsResponse;
import dev.ash.techradar.domain.analytics.dtos.RadarChangesResponse;
import dev.ash.techradar.domain.analytics.dtos.RingMetricsResponse;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.time.LocalDateTime;

public interface AnalyticsService {

  QuadrantMetricsResponse getQuadrantMetrics();

  RingMetricsResponse getRingMetrics();

  RadarChangesResponse getRecentChanges(LocalDateTime since, Quadrant quadrant, Ring ring);
}