package dev.ash.techradar.domain.quadrant.services;

import dev.ash.techradar.domain.quadrant.dtos.QuadrantListResponse;
import dev.ash.techradar.domain.quadrant.dtos.QuadrantTechnologyResponse;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.util.Optional;

public interface QuadrantService {

  /**
   * Retrieve all quadrants with their technology distributions
   *
   * @param ringFilter Optional ring filter to get technologies by ring within quadrants
   * @return QuadrantListResponse containing quadrant summaries
   */
  QuadrantListResponse getAllQuadrants(Optional<Ring> ringFilter);

  /**
   * Get technologies for a specific quadrant
   *
   * @param quadrantType The quadrant to filter by
   * @param ringFilter   Optional ring filter
   * @param search       Optional search term
   * @return QuadrantTechnologyResponse containing filtered technologies
   */
  QuadrantTechnologyResponse getTechnologiesByQuadrant(
      Quadrant quadrantType,
      Optional<Ring> ringFilter,
      Optional<String> search
  );
}