package dev.ash.techradar.domain.technology.exceptions;

/**
 * Exception thrown when technology data is invalid.
 */
public class InvalidTechnologyDataException extends RuntimeException {

  public InvalidTechnologyDataException(String message) {
    super(message);
  }
}
