package dev.ash.techradar.domain.quadrant.dtos;

import java.util.List;

import lombok.Data;

@Data
public class QuadrantListResponse {

    private List<QuadrantSummaryResponse> quadrants;
}
