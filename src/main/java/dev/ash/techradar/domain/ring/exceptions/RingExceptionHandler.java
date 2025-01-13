package dev.ash.techradar.domain.ring.exceptions;

import dev.ash.techradar.common.dtos.errorhandling.ErrorResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class RingExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRingType(MethodArgumentTypeMismatchException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setTimestamp(LocalDateTime.now());
    error.setStatus(HttpStatus.BAD_REQUEST.value());
    error.setError("Invalid Ring Type");
    error.setMessage("Valid ring types are: ADOPT, TRIAL, ASSESS, HOLD");
    error.setPath(ex.getParameter().getExecutable().getDeclaringClass().getSimpleName());
    error.setTraceId(UUID.randomUUID().toString());

    return ResponseEntity.badRequest().body(error);
  }
}