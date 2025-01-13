package dev.ash.techradar.domain.ring.dtos;

import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.util.List;
import lombok.Data;

@Data
public class RingTechnologyResponse {

  private Ring ringType;
  private List<TechnologyResponse> technologies;
  private int totalTechnologies;
}