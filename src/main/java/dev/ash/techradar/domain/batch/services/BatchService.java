package dev.ash.techradar.domain.batch.services;

import dev.ash.techradar.domain.batch.dtos.BatchCreateRequest;
import dev.ash.techradar.domain.batch.dtos.BatchOperationResponse;
import dev.ash.techradar.domain.batch.dtos.BatchUpdateRequest;

public interface BatchService {

    BatchOperationResponse createTechnologies(BatchCreateRequest request);

    BatchOperationResponse updateTechnologies(BatchUpdateRequest request);
}
