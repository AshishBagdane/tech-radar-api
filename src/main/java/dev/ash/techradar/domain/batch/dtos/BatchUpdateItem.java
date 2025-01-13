package dev.ash.techradar.domain.batch.dtos;

import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class BatchUpdateItem {

  @NotNull
  private UUID id;
  private UpdateTechnologyRequest updates;
}