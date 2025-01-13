package dev.ash.techradar.domain.technology.dtos;

import java.util.List;
import lombok.Data;

@Data
public class TechnologyListResponse {

  private List<TechnologyResponse> content;
  private dev.ash.techradar.domain.technology.dtos.PageMetadata page;
}