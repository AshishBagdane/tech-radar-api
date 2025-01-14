package dev.ash.techradar.domain.quadrant.dtos;

import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.Data;

import java.util.Map;

@Data
public class QuadrantSummaryResponse {

    private Quadrant quadrantType;

    private Map<Ring, Integer> technologiesByRing;

    private int totalTechnologies;
}
