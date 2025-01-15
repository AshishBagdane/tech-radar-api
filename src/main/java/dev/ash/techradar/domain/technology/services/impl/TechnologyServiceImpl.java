package dev.ash.techradar.domain.technology.services.impl;

import dev.ash.techradar.common.enums.SortDirection;
import dev.ash.techradar.common.error.exception.resource.ResourceAlreadyExistsException;
import dev.ash.techradar.common.error.exception.resource.ResourceNotFoundException;
import dev.ash.techradar.common.error.exception.validation.BusinessRuleViolationException;
import dev.ash.techradar.common.error.exception.validation.InvalidInputException;
import dev.ash.techradar.common.error.metrics.ErrorMetrics;
import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.PageMetadata;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Ring;
import dev.ash.techradar.domain.technology.mappers.TechnologyMapper;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import dev.ash.techradar.domain.technology.services.TechnologyService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyRepository technologyRepository;

    private final TechnologyMapper technologyMapper;

    private final ErrorMetrics errorMetrics;

    private static final int MAX_NAME_LENGTH = 100;

    private static final int MAX_DESCRIPTION_LENGTH = 500;

    private static final int MAX_METADATA_ENTRIES = 20;

    private static final int MAX_METADATA_KEY_LENGTH = 50;

    private static final int MAX_METADATA_VALUE_LENGTH = 200;

    @Override
    public TechnologyListResponse listTechnologies(TechnologyFilter filter, int page, int size) {
        validatePaginationParams(page, size);
        validateFilter(filter);

        PageRequest pageRequest = createPageRequest(filter, page, size);
        Specification<Technology> spec = createSpecification(filter);

        Page<Technology> technologies = technologyRepository.findAll(spec, pageRequest);
        return createListResponse(technologies);
    }

    @Override
    public TechnologyResponse getTechnology(UUID id) {
        validateId(id);
        Technology technology = findTechnologyById(id);
        return technologyMapper.toResponse(technology);
    }

    @Override
    @Transactional
    public TechnologyResponse createTechnology(CreateTechnologyRequest request) {
        validateCreateRequest(request);

        Technology technology = technologyMapper.toEntity(request);
        technology = technologyRepository.save(technology);

        log.info("Created new technology: {}", technology.getName());
        return technologyMapper.toResponse(technology);
    }

    @Override
    @Transactional
    public TechnologyResponse updateTechnology(UUID id, UpdateTechnologyRequest request) {
        validateId(id);
        validateUpdateRequest(request, id);

        Technology technology = findTechnologyById(id);
        technologyMapper.updateEntity(technology, request);
        technology = technologyRepository.save(technology);

        log.info("Updated technology: {}", technology.getName());
        return technologyMapper.toResponse(technology);
    }

    @Override
    @Transactional
    public void deleteTechnology(UUID id) {
        validateId(id);
        Technology technology = findTechnologyById(id);

        // Validate business rules before deletion
        validateDeletion(technology);

        technologyRepository.delete(technology);
        log.info("Deleted technology: {}", technology.getName());
    }

    @Override
    public boolean existsByName(String name, UUID excludeId) {
        if (excludeId != null) {
            return technologyRepository.existsByNameAndIdNot(name, excludeId);
        }
        return technologyRepository.existsByName(name);
    }

    private void validateId(UUID id) {
        if (id == null) {
            throw new InvalidInputException("id", "null", "Technology ID cannot be null");
        }
    }

    private void validateCreateRequest(CreateTechnologyRequest request) {
        Map<String, String> validationErrors = new HashMap<>();

        // Validate required fields
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            validationErrors.put("name", "Name is required");
        } else if (request.getName().length() > MAX_NAME_LENGTH) {
            validationErrors.put("name", "Name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }

        if (request.getQuadrant() == null) {
            validationErrors.put("quadrant", "Quadrant is required");
        }

        if (request.getRing() == null) {
            validationErrors.put("ring", "Ring is required");
        }

        // Validate description length if present
        if (StringUtils.isNotEmpty(request.getDescription()) &&
            request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            validationErrors.put("description",
                                 "Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters");
        }

        // Validate metadata if present
        if (request.getMetadata() != null) {
            validateMetadata(request.getMetadata(), validationErrors);
        }

        if (!validationErrors.isEmpty()) {
            errorMetrics.recordValidationErrors("technology", validationErrors.size());
            throw new InvalidInputException(validationErrors);
        }

        // Check for duplicate name
        if (existsByName(request.getName(), null)) {
            throw new ResourceAlreadyExistsException("Technology", request.getName());
        }
    }

    private void validateUpdateRequest(UpdateTechnologyRequest request, UUID id) {
        Map<String, String> validationErrors = new HashMap<>();

        // Validate name if present
        if (request.getName() != null) {
            if (request.getName().trim().isEmpty()) {
                validationErrors.put("name", "Name cannot be empty");
            } else if (request.getName().length() > MAX_NAME_LENGTH) {
                validationErrors.put("name", "Name cannot exceed " + MAX_NAME_LENGTH + " characters");
            }
        }

        // Validate description if present
        if (StringUtils.isNotEmpty(request.getDescription()) &&
            request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            validationErrors.put("description",
                                 "Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters");
        }

        // Validate metadata if present
        if (request.getMetadata() != null) {
            validateMetadata(request.getMetadata(), validationErrors);
        }

        if (!validationErrors.isEmpty()) {
            errorMetrics.recordValidationErrors("technology", validationErrors.size());
            throw new InvalidInputException(validationErrors);
        }

        // Check for duplicate name if name is being updated
        if (request.getName() != null && existsByName(request.getName(), id)) {
            throw new ResourceAlreadyExistsException("Technology", request.getName());
        }
    }

    private void validateMetadata(Map<String, String> metadata, Map<String, String> validationErrors) {
        if (metadata.size() > MAX_METADATA_ENTRIES) {
            validationErrors.put("metadata",
                                 "Cannot exceed " + MAX_METADATA_ENTRIES + " metadata entries");
            return;
        }

        metadata.forEach((key, value) -> {
            if (key == null || key.trim().isEmpty()) {
                validationErrors.put("metadata", "Metadata key cannot be empty");
            } else if (key.length() > MAX_METADATA_KEY_LENGTH) {
                validationErrors.put("metadata",
                                     "Metadata key cannot exceed " + MAX_METADATA_KEY_LENGTH + " characters");
            }

            if (value != null && value.length() > MAX_METADATA_VALUE_LENGTH) {
                validationErrors.put("metadata",
                                     "Metadata value cannot exceed " + MAX_METADATA_VALUE_LENGTH + " characters");
            }
        });
    }

    private void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new InvalidInputException("page", String.valueOf(page), "Page index must not be negative");
        }
        if (size < 1 || size > 100) {
            throw new InvalidInputException("size", String.valueOf(size), "Page size must be between 1 and 100");
        }
    }

    private void validateFilter(TechnologyFilter filter) {
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

    private void validateDeletion(Technology technology) {
        // Example business rule: Cannot delete technology in ADOPT ring
        if (technology.getRing() == Ring.ADOPT) {
            throw new BusinessRuleViolationException(
                "DELETE_ADOPTED_TECHNOLOGY",
                "Cannot delete technology in ADOPT ring",
                Arrays.asList("technologyId", "technologyName", "ring")
            );
        }
    }

    private Technology findTechnologyById(UUID id) {
        return technologyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Technology",
                "id",
                id.toString()
            ));
    }

    private PageRequest createPageRequest(TechnologyFilter filter, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt"); // Default sorting
        if (filter.getSortBy() != null) {
            sort = Sort.by(filter.getSortDirection() == SortDirection.ASC ?
                               Sort.Direction.ASC : Sort.Direction.DESC,
                           filter.getSortBy());
        }
        return PageRequest.of(page, size, sort);
    }

    private Specification<Technology> createSpecification(TechnologyFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (filter.getQuadrant() != null) {
                predicates.add(cb.equal(root.get("quadrant"), filter.getQuadrant()));
            }

            if (filter.getRing() != null) {
                predicates.add(cb.equal(root.get("ring"), filter.getRing()));
            }

            if (filter.getSearch() != null) {
                String search = "%" + filter.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), search),
                    cb.like(cb.lower(root.get("description")), search)
                ));
            }

            if (filter.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                    root.get("updatedAt"), filter.getFromDate()));
            }

            if (filter.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                    root.get("updatedAt"), filter.getToDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private TechnologyListResponse createListResponse(Page<Technology> page) {
        List<TechnologyResponse> technologies = page.getContent().stream()
            .map(technologyMapper::toResponse)
            .toList();

        TechnologyListResponse response = new TechnologyListResponse();
        response.setContent(technologies);

        PageMetadata metadata = new PageMetadata();
        metadata.setNumber(page.getNumber());
        metadata.setSize(page.getSize());
        metadata.setTotalElements(page.getTotalElements());
        metadata.setTotalPages(page.getTotalPages());

        response.setPage(metadata);
        return response;
    }
}
