package dev.ash.techradar.domain.batch.api;

import dev.ash.techradar.domain.batch.dtos.BatchCreateRequest;
import dev.ash.techradar.domain.batch.dtos.BatchOperationResponse;
import dev.ash.techradar.domain.batch.dtos.BatchUpdateRequest;
import dev.ash.techradar.domain.batch.services.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/technologies/batch")
@Tag(name = "Batch Operations", description = "APIs for batch creating and updating technologies")
@RequiredArgsConstructor
public class BatchController {

  private final BatchService batchService;

  @PostMapping
  @Operation(summary = "Create multiple technologies in a batch")
  public ResponseEntity<BatchOperationResponse> batchCreate(
      @Valid @RequestBody BatchCreateRequest request) {
    log.info("Processing batch create request for {} technologies",
        request.getTechnologies().size());
    return ResponseEntity.ok(batchService.createTechnologies(request));
  }

  @PutMapping
  @Operation(summary = "Update multiple technologies in a batch")
  public ResponseEntity<BatchOperationResponse> batchUpdate(
      @Valid @RequestBody BatchUpdateRequest request) {
    log.info("Processing batch update request for {} technologies",
        request.getTechnologies().size());
    return ResponseEntity.ok(batchService.updateTechnologies(request));
  }
}