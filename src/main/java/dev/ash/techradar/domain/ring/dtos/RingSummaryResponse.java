package dev.ash.techradar.domain.ring.dtos;

import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.util.Map;
import lombok.Data;

@Data
public class RingSummaryResponse {

  private Ring ringType;
  private Map<Quadrant, Integer> technologiesByQuadrant;
  private int totalTechnologies;
}