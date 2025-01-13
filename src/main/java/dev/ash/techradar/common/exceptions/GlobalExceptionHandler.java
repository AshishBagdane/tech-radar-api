package dev.ash.techradar.common.exceptions;

import dev.ash.techradar.common.dtos.errorhandling.ErrorResponse;
import dev.ash.techradar.domain.quadrant.exceptions.InvalidQuadrantException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidQuadrantException.class)
  public ResponseEntity<ErrorResponse> handleInvalidQuadrantException(InvalidQuadrantException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setTimestamp(LocalDateTime.now());
    error.setStatus(HttpStatus.BAD_REQUEST.value());
    error.setError("Invalid Quadrant");
    error.setMessage(ex.getMessage());
    error.setTraceId(UUID.randomUUID().toString());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // Add other exception handlers as needed
}