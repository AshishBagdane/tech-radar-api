package dev.ash.techradar.domain.batch.dtos;

import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class BatchCreateRequest {

  @NotNull
  @Size(min = 1, max = 100)
  private List<CreateTechnologyRequest> technologies;
}