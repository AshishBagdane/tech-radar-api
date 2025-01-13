package dev.ash.techradar.domain.technology.dtos;

import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class TechnologyResponse {

  private UUID id;
  private String name;
  private String description;
  private Quadrant quadrant;
  private Ring ring;
  private Map<String, String> metadata;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}