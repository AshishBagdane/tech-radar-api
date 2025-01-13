package dev.ash.techradar.common.dtos;

import dev.ash.techradar.common.enums.SortDirection;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.time.LocalDateTime;
import lombok.Data;

@Data
class TechnologyFilter {

  private String search;
  private Quadrant quadrant;
  private Ring ring;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private String sortBy;
  private SortDirection sortDirection;
}