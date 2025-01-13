package dev.ash.techradar.domain.technology.dtos;

import lombok.Data;

@Data
public class PageMetadata {

  private int number;
  private int size;
  private long totalElements;
  private int totalPages;
}