package dev.ash.techradar.domain.technology.services.impl;

import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.repositories.TechnologyRepository;
import dev.ash.techradar.domain.technology.services.TechnologyService;
import dev.ash.techradar.domain.technology.services.support.TechnologyResponseAssembler;
import dev.ash.techradar.domain.technology.services.support.TechnologyValidator;
import dev.ash.techradar.domain.technology.specifications.TechnologySpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyRepository technologyRepository;

    private final TechnologyValidator validator;

    private final TechnologyResponseAssembler responseAssembler;

    private final TechnologySpecificationBuilder specificationBuilder;

    @Override
    public TechnologyListResponse listTechnologies(TechnologyFilter filter, int page, int size) {
        validator.validatePaginationParams(page, size);
        validator.validateFilter(filter);

        PageRequest pageRequest = validator.createPageRequest(filter, page, size);
        Specification<Technology> spec = specificationBuilder.createSpecification(filter);

        Page<Technology> technologies = technologyRepository.findAll(spec, pageRequest);
        return responseAssembler.createListResponse(technologies);
    }

    @Override
    public TechnologyResponse getTechnology(UUID id) {
        validator.validateId(id);
        Technology technology = findTechnologyById(id);
        return responseAssembler.toResponse(technology);
    }

    @Override
    @Transactional
    public TechnologyResponse createTechnology(CreateTechnologyRequest request) {
        validator.validateCreateRequest(request);

        Technology technology = responseAssembler.toEntity(request);
        technology = technologyRepository.save(technology);

        log.info("Created new technology: {}", technology.getName());
        return responseAssembler.toResponse(technology);
    }

    @Override
    @Transactional
    public TechnologyResponse updateTechnology(UUID id, UpdateTechnologyRequest request) {
        validator.validateId(id);
        validator.validateUpdateRequest(request, id);

        Technology technology = findTechnologyById(id);
        responseAssembler.updateEntity(technology, request);
        technology = technologyRepository.save(technology);

        log.info("Updated technology: {}", technology.getName());
        return responseAssembler.toResponse(technology);
    }

    @Override
    @Transactional
    public void deleteTechnology(UUID id) {
        validator.validateId(id);
        Technology technology = findTechnologyById(id);

        validator.validateDeletion(technology);

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
            .orElseThrow(() -> validator.createResourceNotFoundException(id));
    }
}
