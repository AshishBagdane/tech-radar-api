package dev.ash.techradar.domain.quadrant.dtos;

import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import java.util.List;
import lombok.Data;

@Data
public class QuadrantTechnologyResponse {

  private Quadrant quadrantType;
  private List<TechnologyResponse> technologies;
  private int totalTechnologies;
}