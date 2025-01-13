package dev.ash.techradar.domain.technology.dtos;

import dev.ash.techradar.common.enums.SortDirection;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Filter criteria for technology queries. Supports filtering by quadrant, ring, search term, date range, and sorting options.
 */
@Data
public class TechnologyFilter {

  @Size(max = 100)
  private String search;

  private Quadrant quadrant;

  private Ring ring;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime fromDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime toDate;

  private String sortBy;

  private SortDirection sortDirection = SortDirection.DESC;
}