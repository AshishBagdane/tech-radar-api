package dev.ash.techradar.domain.batch.dtos;

import java.util.List;
import lombok.Data;

@Data
public class BatchOperationResponse {

  private int successCount;
  private int failureCount;
  private List<BatchOperationError> errors;
}