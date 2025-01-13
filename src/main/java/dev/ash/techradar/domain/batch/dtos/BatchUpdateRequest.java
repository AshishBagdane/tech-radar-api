package dev.ash.techradar.domain.batch.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class BatchUpdateRequest {

  @NotNull
  @Size(min = 1, max = 100)
  private List<BatchUpdateItem> technologies;
}