package dev.ash.techradar.domain.batch.exceptions;

public class BatchOperationException extends RuntimeException {

  public BatchOperationException(String message) {
    super(message);
  }

  public BatchOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}