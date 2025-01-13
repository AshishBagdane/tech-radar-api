package dev.ash.techradar.domain.technology.services.impl;

import dev.ash.techradar.common.enums.SortDirection;
import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.PageMetadata;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.exceptions.DuplicateTechnologyException;
import dev.ash.techradar.domain.technology.exceptions.TechnologyNotFoundException;
import dev.ash.techradar.domain.technology.mappers.TechnologyMapper;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import dev.ash.techradar.domain.technology.services.TechnologyService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechnologyServiceImpl implements TechnologyService {

  private final TechnologyRepository technologyRepository;
  private final TechnologyMapper technologyMapper;

  @Override
  public TechnologyListResponse listTechnologies(TechnologyFilter filter, int page, int size) {
    PageRequest pageRequest = createPageRequest(filter, page, size);
    Specification<Technology> spec = createSpecification(filter);

    Page<Technology> technologies = technologyRepository.findAll(spec, pageRequest);

    return createListResponse(technologies);
  }

  @Override
  public TechnologyResponse getTechnology(UUID id) {
    Technology technology = findTechnologyById(id);
    return technologyMapper.toResponse(technology);
  }

  @Override
  @Transactional
  public TechnologyResponse createTechnology(CreateTechnologyRequest request) {
    validateNewTechnology(request);

    Technology technology = technologyMapper.toEntity(request);
    technology = technologyRepository.save(technology);

    log.info("Created new technology: {}", technology.getName());
    return technologyMapper.toResponse(technology);
  }

  @Override
  @Transactional
  public TechnologyResponse updateTechnology(UUID id, UpdateTechnologyRequest request) {
    Technology technology = findTechnologyById(id);
    validateUpdateTechnology(request, id);

    technologyMapper.updateEntity(technology, request);
    technology = technologyRepository.save(technology);

    log.info("Updated technology: {}", technology.getName());
    return technologyMapper.toResponse(technology);
  }

  @Override
  @Transactional
  public void deleteTechnology(UUID id) {
    Technology technology = findTechnologyById(id);
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

  private Technology findTechnologyById(UUID id) {
    return technologyRepository.findById(id)
        .orElseThrow(() -> new TechnologyNotFoundException(id));
  }

  private void validateNewTechnology(CreateTechnologyRequest request) {
    if (existsByName(request.getName(), null)) {
      throw new DuplicateTechnologyException(request.getName());
    }
  }

  private void validateUpdateTechnology(UpdateTechnologyRequest request, UUID id) {
    if (request.getName() != null && existsByName(request.getName(), id)) {
      throw new DuplicateTechnologyException(request.getName());
    }
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
        .collect(Collectors.toList());

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