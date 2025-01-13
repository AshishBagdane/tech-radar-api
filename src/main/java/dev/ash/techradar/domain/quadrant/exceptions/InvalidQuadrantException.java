package dev.ash.techradar.domain.quadrant.exceptions;

/**
 * Exception thrown when technology data is invalid.
 */
public class InvalidQuadrantException extends RuntimeException {

  public InvalidQuadrantException(String message) {
    super(message);
  }
}
