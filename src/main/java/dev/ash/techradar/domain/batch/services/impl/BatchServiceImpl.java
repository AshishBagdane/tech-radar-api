package dev.ash.techradar.domain.batch.services.impl;

import dev.ash.techradar.domain.batch.dtos.BatchCreateRequest;
import dev.ash.techradar.domain.batch.dtos.BatchOperationError;
import dev.ash.techradar.domain.batch.dtos.BatchOperationResponse;
import dev.ash.techradar.domain.batch.dtos.BatchUpdateItem;
import dev.ash.techradar.domain.batch.dtos.BatchUpdateRequest;
import dev.ash.techradar.domain.batch.services.BatchService;
import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.mappers.TechnologyMapper;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

  private final TechnologyRepository technologyRepository;
  private final TechnologyMapper technologyMapper;

  @Override
  @Transactional
  public BatchOperationResponse createTechnologies(BatchCreateRequest request) {
    List<BatchOperationError> errors = new ArrayList<>();
    List<Technology> successfulTechnologies = new ArrayList<>();

    for (CreateTechnologyRequest techRequest : request.getTechnologies()) {
      try {
        Technology technology = technologyMapper.toEntity(techRequest);
        successfulTechnologies.add(technology);
      } catch (Exception e) {
        log.error("Error creating technology: {}", techRequest.getName(), e);
        errors.add(new BatchOperationError(null,
            "Failed to create technology: " + techRequest.getName(),
            "CREATE_ERROR"));
      }
    }

    // Save all successful technologies in a batch
    if (!successfulTechnologies.isEmpty()) {
      technologyRepository.saveAll(successfulTechnologies);
    }

    return createBatchResponse(successfulTechnologies.size(), errors);
  }

  @Override
  @Transactional
  public BatchOperationResponse updateTechnologies(BatchUpdateRequest request) {
    List<BatchOperationError> errors = new ArrayList<>();
    List<Technology> successfulUpdates = new ArrayList<>();

    // First, fetch all technologies that need to be updated
    Set<UUID> techIds = request.getTechnologies().stream()
        .map(BatchUpdateItem::getId)
        .collect(Collectors.toSet());

    Map<UUID, Technology> existingTechs = technologyRepository.findAllById(techIds)
        .stream()
        .collect(Collectors.toMap(Technology::getId, tech -> tech));

    for (BatchUpdateItem updateItem : request.getTechnologies()) {
      try {
        Technology existing = existingTechs.get(updateItem.getId());
        if (existing == null) {
          errors.add(new BatchOperationError(updateItem.getId(),
              "Technology not found",
              "NOT_FOUND"));
          continue;
        }

        technologyMapper.updateEntity(existing, updateItem.getUpdates());
        successfulUpdates.add(existing);
      } catch (Exception e) {
        log.error("Error updating technology: {}", updateItem.getId(), e);
        errors.add(new BatchOperationError(updateItem.getId(),
            "Failed to update technology",
            "UPDATE_ERROR"));
      }
    }

    // Save all successful updates in a batch
    if (!successfulUpdates.isEmpty()) {
      technologyRepository.saveAll(successfulUpdates);
    }

    return createBatchResponse(successfulUpdates.size(), errors);
  }

  private BatchOperationResponse createBatchResponse(int successCount, List<BatchOperationError> errors) {
    BatchOperationResponse response = new BatchOperationResponse();
    response.setSuccessCount(successCount);
    response.setFailureCount(errors.size());
    response.setErrors(errors);
    return response;
  }
}