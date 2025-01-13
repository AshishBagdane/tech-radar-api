package dev.ash.techradar.domain.technology.exceptions;

import java.util.UUID;

/**
 * Exception thrown when a technology is not found.
 */
public class TechnologyNotFoundException extends RuntimeException {

  public TechnologyNotFoundException(UUID id) {
    super("Technology not found with id: " + id);
  }
}
