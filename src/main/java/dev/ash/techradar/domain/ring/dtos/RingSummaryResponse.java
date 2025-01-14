package dev.ash.techradar.domain.ring.dtos;

import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.Data;

import java.util.Map;

@Data
public class RingSummaryResponse {

    private Ring ringType;

    private Map<Quadrant, Integer> technologiesByQuadrant;

    private int totalTechnologies;
}
