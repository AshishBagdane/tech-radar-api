package dev.ash.techradar.domain.batch.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BatchOperationResponse {

    private int successCount;

    private int failureCount;

    private List<BatchOperationError> errors;
}
