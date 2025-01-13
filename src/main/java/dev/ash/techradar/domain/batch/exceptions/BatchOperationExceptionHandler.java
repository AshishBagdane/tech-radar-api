package dev.ash.techradar.domain.batch.exceptions;

import dev.ash.techradar.common.dtos.errorhandling.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BatchOperationExceptionHandler {

  @ExceptionHandler(BatchOperationException.class)
  public ResponseEntity<ErrorResponse> handleBatchOperationException(BatchOperationException ex) {
    log.error("Batch operation failed", ex);
    ErrorResponse error = new ErrorResponse();
    error.setStatus(400);
    error.setError("Batch Operation Failed");
    error.setMessage(ex.getMessage());
    return ResponseEntity.badRequest().body(error);
  }
}