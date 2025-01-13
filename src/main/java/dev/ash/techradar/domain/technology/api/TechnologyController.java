package dev.ash.techradar.domain.technology.api;

import dev.ash.techradar.common.dtos.errorhandling.ErrorResponse;
import dev.ash.techradar.domain.technology.dtos.CreateTechnologyRequest;
import dev.ash.techradar.domain.technology.dtos.TechnologyFilter;
import dev.ash.techradar.domain.technology.dtos.TechnologyListResponse;
import dev.ash.techradar.domain.technology.dtos.TechnologyResponse;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.exceptions.DuplicateTechnologyException;
import dev.ash.techradar.domain.technology.exceptions.TechnologyNotFoundException;
import dev.ash.techradar.domain.technology.services.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<TechnologyListResponse> listTechnologies(
      @Valid TechnologyFilter filter,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    log.debug("Fetching technologies with filter: {} and pagination: page={}, size={}", filter, page, size);
    TechnologyListResponse response = technologyService.listTechnologies(filter, page, size);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get technology details by ID")
  public ResponseEntity<TechnologyResponse> getTechnology(@PathVariable UUID id) {
    log.debug("Fetching technology with id: {}", id);
    TechnologyResponse response = technologyService.getTechnology(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  @Operation(summary = "Add a new technology")
  public ResponseEntity<TechnologyResponse> createTechnology(
      @RequestBody @Valid CreateTechnologyRequest request) {
    log.debug("Creating new technology: {}", request);
    TechnologyResponse response = technologyService.createTechnology(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing technology")
  public ResponseEntity<TechnologyResponse> updateTechnology(
      @PathVariable UUID id,
      @RequestBody @Valid UpdateTechnologyRequest request) {
    log.debug("Updating technology with id: {}", id);
    TechnologyResponse response = technologyService.updateTechnology(id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a technology")
  public ResponseEntity<Void> deleteTechnology(@PathVariable UUID id) {
    log.debug("Deleting technology with id: {}", id);
    technologyService.deleteTechnology(id);
    return ResponseEntity.noContent().build();
  }

  // Exception Handlers for technology-specific errors
  @ExceptionHandler(TechnologyNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTechnologyNotFound(TechnologyNotFoundException ex,
      HttpServletRequest request) {
    log.error("Technology not found: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Technology Not Found")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .traceId(MDC.get("traceId"))
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(DuplicateTechnologyException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateTechnology(DuplicateTechnologyException ex,
      HttpServletRequest request) {
    log.error("Duplicate technology: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.CONFLICT.value())
        .error("Duplicate Technology")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .traceId(MDC.get("traceId"))
        .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }
}