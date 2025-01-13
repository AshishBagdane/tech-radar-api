package dev.ash.techradar.domain.technology.exceptions;

/**
 * Exception thrown when attempting to create a duplicate technology.
 */
public class DuplicateTechnologyException extends RuntimeException {

  public DuplicateTechnologyException(String name) {
    super("Technology already exists with name: " + name);
  }
}
