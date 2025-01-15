package dev.ash.techradar.domain.technology.api;

import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.services.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/technologies")
@RequiredArgsConstructor
@Validated
@Tag(name = "Technology Management", description = "APIs for managing technologies in the Tech Radar")
@Slf4j
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping
    @Operation(summary = "List all technologies with optional filtering and pagination")
    public TechnologyListResponse listTechnologies(
        @Valid TechnologyFilter filter,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        log.debug("Fetching technologies with filter: {} and pagination: page={}, size={}", filter, page, size);
        return technologyService.listTechnologies(filter, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get technology details by ID")
    public TechnologyResponse getTechnology(@PathVariable UUID id) {
        log.debug("Fetching technology with id: {}", id);
        return technologyService.getTechnology(id);
    }

    @PostMapping
    @Operation(summary = "Add a new technology")
    public TechnologyResponse createTechnology(
        @RequestBody @Valid CreateTechnologyRequest request) {
        log.debug("Creating new technology: {}", request);
        return technologyService.createTechnology(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing technology")
    public TechnologyResponse updateTechnology(
        @PathVariable UUID id,
        @RequestBody @Valid UpdateTechnologyRequest request) {
        log.debug("Updating technology with id: {}", id);
        return technologyService.updateTechnology(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a technology")
    public void deleteTechnology(@PathVariable UUID id) {
        log.debug("Deleting technology with id: {}", id);
        technologyService.deleteTechnology(id);
    }
}
