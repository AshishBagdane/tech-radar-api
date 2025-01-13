package dev.ash.techradar.domain.technology.repositories;

import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, UUID>,
    JpaSpecificationExecutor<Technology> {

  boolean existsByName(String name);

  boolean existsByNameAndIdNot(String name, UUID id);

  @Query("SELECT t FROM Technology t WHERE " +
      "(:quadrant IS NULL OR t.quadrant = :quadrant) AND " +
      "(:ring IS NULL OR t.ring = :ring) AND " +
      "(:search IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
      "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
  List<Technology> findByFilters(
      @Param("quadrant") Quadrant quadrant,
      @Param("ring") Ring ring,
      @Param("search") String search
  );

  @Query("SELECT COUNT(t) FROM Technology t WHERE t.quadrant = :quadrant")
  long countByQuadrant(@Param("quadrant") Quadrant quadrant);

  @Query("SELECT COUNT(t) FROM Technology t WHERE t.ring = :ring")
  long countByRing(@Param("ring") Ring ring);

  List<Technology> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

  Optional<Technology> findByNameIgnoreCase(String name);

  List<Technology> findByRing(Ring ring);

  @Query("SELECT t FROM Technology t WHERE t.quadrant = :quadrant " +
      "AND (:ring IS NULL OR t.ring = :ring) " +
      "AND (:search IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')))")
  List<Technology> findByQuadrantAndFilters(Quadrant quadrant, Ring ring, String search);

  int countByRingAndQuadrant(Ring ring, Quadrant quadrant);

  @Query("SELECT t FROM Technology t WHERE t.ring = :ring " +
      "AND (:quadrant IS NULL OR t.quadrant = :quadrant) " +
      "AND (:search IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
      "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
      "AND (:fromDate IS NULL OR t.createdAt >= :fromDate) " +
      "AND (:toDate IS NULL OR t.createdAt <= :toDate)")
  List<Technology> findByRingWithFilters(
      @Param("ring") Ring ring,
      @Param("quadrant") Quadrant quadrant,
      @Param("search") String search,
      @Param("fromDate") LocalDateTime fromDate,
      @Param("toDate") LocalDateTime toDate
  );
}