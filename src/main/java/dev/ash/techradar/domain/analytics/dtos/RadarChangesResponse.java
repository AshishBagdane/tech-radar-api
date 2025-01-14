package dev.ash.techradar.domain.analytics.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RadarChangesResponse {

    private List<TechnologyChangeResponse> changes;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private int totalChanges;
}
