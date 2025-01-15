package dev.ash.techradar.domain.technology.services;

import dev.ash.techradar.common.error.exception.resource.ResourceAlreadyExistsException;
import dev.ash.techradar.common.error.exception.resource.ResourceNotFoundException;
import dev.ash.techradar.common.error.exception.validation.InvalidInputException;
import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;

import java.util.UUID;

/**
 * Service interface for managing technologies in the Tech Radar. Provides operations for creating, reading, updating,
 * and deleting technologies, as well as filtering and pagination support.
 */
public interface TechnologyService {

    /**
     * Retrieves a paginated list of technologies based on the provided filter criteria.
     *
     * @param filter Filter criteria for technologies
     * @param page   Page number (0-based)
     * @param size   Page size
     * @return Paginated list of technologies
     */
    TechnologyListResponse listTechnologies(TechnologyFilter filter, int page, int size);

    /**
     * Retrieves a specific technology by its ID.
     *
     * @param id Technology ID
     * @return Technology details
     * @throws ResourceNotFoundException if technology is not found
     */
    TechnologyResponse getTechnology(UUID id);

    /**
     * Creates a new technology entry.
     *
     * @param request Technology creation details
     * @return Created technology
     * @throws ResourceAlreadyExistsException if technology with same name exists
     * @throws InvalidInputException          if request data is invalid
     */
    TechnologyResponse createTechnology(CreateTechnologyRequest request);

    /**
     * Updates an existing technology.
     *
     * @param id      Technology ID
     * @param request Technology update details
     * @return Updated technology
     * @throws ResourceNotFoundException      if technology is not found
     * @throws ResourceAlreadyExistsException if update would create name conflict
     * @throws InvalidInputException          if request data is invalid
     */
    TechnologyResponse updateTechnology(UUID id, UpdateTechnologyRequest request);

    /**
     * Deletes a technology by its ID.
     *
     * @param id Technology ID
     * @throws ResourceNotFoundException if technology is not found
     */
    void deleteTechnology(UUID id);

    /**
     * Checks if a technology with the given name exists.
     *
     * @param name      Technology name
     * @param excludeId Optional ID to exclude from the check (for updates)
     * @return true if technology exists, false otherwise
     */
    boolean existsByName(String name, UUID excludeId);
}

