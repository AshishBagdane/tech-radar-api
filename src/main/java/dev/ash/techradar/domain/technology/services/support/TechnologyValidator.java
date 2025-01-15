package dev.ash.techradar.domain.technology.services.support;

import dev.ash.techradar.common.enums.SortDirection;
import dev.ash.techradar.common.error.exception.resource.ResourceNotFoundException;
import dev.ash.techradar.common.error.exception.validation.BusinessRuleViolationException;
import dev.ash.techradar.common.error.exception.validation.InvalidInputException;
import dev.ash.techradar.common.error.metrics.ErrorMetrics;
import dev.ash.techradar.domain.technology.config.TechnologyProperties;
import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Ring;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TechnologyValidator {

    private final TechnologyProperties properties;

    private final ErrorMetrics errorMetrics;

    public void validateId(UUID id) {
        if (id == null) {
            throw new InvalidInputException("id", "null", "Technology ID cannot be null");
        }
    }

    public void validateCreateRequest(CreateTechnologyRequest request) {
        Map<String, String> validationErrors = new HashMap<>();

        validateName(request.getName(), validationErrors);

        if (request.getQuadrant() == null) {
            validationErrors.put("quadrant", "Quadrant is required");
        }

        if (request.getRing() == null) {
            validationErrors.put("ring", "Ring is required");
        }

        validateDescription(request.getDescription(), validationErrors);
        validateMetadata(request.getMetadata(), validationErrors);

        if (!validationErrors.isEmpty()) {
            errorMetrics.recordValidationErrors("technology", validationErrors.size());
            throw new InvalidInputException(validationErrors);
        }
    }

    public void validateUpdateRequest(UpdateTechnologyRequest request, UUID id) {
        Map<String, String> validationErrors = new HashMap<>();

        if (request.getName() != null) {
            validateName(request.getName(), validationErrors);
        }

        validateDescription(request.getDescription(), validationErrors);
        validateMetadata(request.getMetadata(), validationErrors);

        if (!validationErrors.isEmpty()) {
            errorMetrics.recordValidationErrors("technology", validationErrors.size());
            throw new InvalidInputException(validationErrors);
        }
    }

    public void validateDeletion(Technology technology) {
        if (technology.getRing() == Ring.ADOPT) {
            throw new BusinessRuleViolationException(
                "DELETE_ADOPTED_TECHNOLOGY",
                "Cannot delete technology in ADOPT ring",
                Arrays.asList("Technology cannot be deleted when in ADOPT ring")
            );
        }
    }

    public void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new InvalidInputException("page", String.valueOf(page),
                                            "Page index must not be negative");
        }
        if (size < 1 || size > 100) {
            throw new InvalidInputException("size", String.valueOf(size),
                                            "Page size must be between 1 and 100");
        }
    }

    public void validateFilter(TechnologyFilter filter) {
        if (filter == null) {
            return;
        }

        Map<String, String> validationErrors = new HashMap<>();

        if (filter.getFromDate() != null && filter.getToDate() != null
            && filter.getFromDate().isAfter(filter.getToDate())) {
            validationErrors.put("dateRange", "From date must not be after to date");
        }

        if (filter.getSearch() != null && filter.getSearch().length() > 100) {
            validationErrors.put("search", "Search term cannot exceed 100 characters");
        }

        if (!validationErrors.isEmpty()) {
            throw new InvalidInputException(validationErrors);
        }
    }

    public PageRequest createPageRequest(TechnologyFilter filter, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt"); // Default sorting
        if (filter.getSortBy() != null) {
            sort = Sort.by(filter.getSortDirection() == SortDirection.ASC ?
                               Sort.Direction.ASC : Sort.Direction.DESC,
                           filter.getSortBy());
        }
        return PageRequest.of(page, size, sort);
    }

    public ResourceNotFoundException createResourceNotFoundException(UUID id) {
        return new ResourceNotFoundException("Technology", "id", id.toString());
    }

    private void validateName(String name, Map<String, String> validationErrors) {
        if (name == null || name.trim().isEmpty()) {
            validationErrors.put("name", "Name is required");
        } else if (name.length() > properties.getValidation().getMaxNameLength()) {
            validationErrors.put("name",
                                 "Name cannot exceed " + properties.getValidation().getMaxNameLength() + " characters");
        }
    }

    private void validateDescription(String description, Map<String, String> validationErrors) {
        if (StringUtils.isNotEmpty(description) &&
            description.length() > properties.getValidation().getMaxDescriptionLength()) {
            validationErrors.put("description",
                                 "Description cannot exceed " + properties.getValidation().getMaxDescriptionLength() +
                                     " characters");
        }
    }

    private void validateMetadata(Map<String, String> metadata, Map<String, String> validationErrors) {
        if (metadata == null) {
            return;
        }

        if (metadata.size() > properties.getValidation().getMaxMetadataEntries()) {
            validationErrors.put("metadata",
                                 "Cannot exceed " + properties.getValidation().getMaxMetadataEntries() +
                                     " metadata entries");
            return;
        }

        metadata.forEach((key, value) -> {
            if (key == null || key.trim().isEmpty()) {
                validationErrors.put("metadata", "Metadata key cannot be empty");
            } else if (key.length() > properties.getValidation().getMaxMetadataKeyLength()) {
                validationErrors.put("metadata",
                                     "Metadata key cannot exceed " +
                                         properties.getValidation().getMaxMetadataKeyLength() + " characters");
            }

            if (value != null && value.length() > properties.getValidation().getMaxMetadataValueLength()) {
                validationErrors.put("metadata",
                                     "Metadata value cannot exceed " +
                                         properties.getValidation().getMaxMetadataValueLength() + " characters");
            }
        });
    }
}
