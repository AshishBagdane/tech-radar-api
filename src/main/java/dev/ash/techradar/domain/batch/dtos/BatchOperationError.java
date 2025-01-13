package dev.ash.techradar.domain.batch.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BatchOperationError {

  private UUID technologyId;
  private String errorMessage;
  private String errorCode;
}