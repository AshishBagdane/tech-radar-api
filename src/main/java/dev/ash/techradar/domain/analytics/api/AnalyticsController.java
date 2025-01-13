package dev.ash.techradar.domain.analytics.api;

import dev.ash.techradar.domain.analytics.dtos.QuadrantMetricsResponse;
import dev.ash.techradar.domain.analytics.dtos.RadarChangesResponse;
import dev.ash.techradar.domain.analytics.dtos.RingMetricsResponse;
import dev.ash.techradar.domain.analytics.services.AnalyticsService;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Validated
@Tag(name = "Analytics", description = "Analytics and Metrics APIs")
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @GetMapping("/quadrant")
  @Operation(summary = "Get technology distribution by quadrant")
  public ResponseEntity<QuadrantMetricsResponse> getQuadrantMetrics() {
    return ResponseEntity.ok(analyticsService.getQuadrantMetrics());
  }

  @GetMapping("/ring")
  @Operation(summary = "Get technology distribution by ring")
  public ResponseEntity<RingMetricsResponse> getRingMetrics() {
    return ResponseEntity.ok(analyticsService.getRingMetrics());
  }

  @GetMapping("/changes")
  @Operation(summary = "Get recent changes/updates to the radar")
  public ResponseEntity<RadarChangesResponse> getRecentChanges(
      @Parameter(description = "Filter changes since date")
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime since,

      @Parameter(description = "Filter by quadrant")
      @RequestParam(required = false)
      Quadrant quadrant,

      @Parameter(description = "Filter by ring")
      @RequestParam(required = false)
      Ring ring) {

    return ResponseEntity.ok(
        analyticsService.getRecentChanges(since, quadrant, ring)
    );
  }
}