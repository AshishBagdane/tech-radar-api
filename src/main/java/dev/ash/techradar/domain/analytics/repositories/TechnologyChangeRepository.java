package dev.ash.techradar.domain.analytics.repositories;

import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyChangeRepository extends JpaRepository<TechnologyChange, UUID> {

  @Query("""
      SELECT tc FROM TechnologyChange tc
      JOIN FETCH tc.technology t
      WHERE (:since IS NULL OR tc.changeDate >= :since)
      AND (:quadrant IS NULL OR t.quadrant = :quadrant)
      AND (:ring IS NULL OR t.ring = :ring)
      ORDER BY tc.changeDate DESC
      """)
  List<TechnologyChange> findRecentChanges(
      @Param("since") LocalDateTime since,
      @Param("quadrant") Quadrant quadrant,
      @Param("ring") Ring ring
  );

  @Query("""
      SELECT COUNT(tc) FROM TechnologyChange tc
      JOIN tc.technology t
      WHERE tc.changeDate >= :since
      AND t.quadrant = :quadrant
      """)
  long countChangesByQuadrantSince(
      @Param("quadrant") Quadrant quadrant,
      @Param("since") LocalDateTime since
  );

  @Query("""
      SELECT COUNT(tc) FROM TechnologyChange tc
      JOIN tc.technology t
      WHERE tc.changeDate >= :since
      AND t.ring = :ring
      """)
  long countChangesByRingSince(
      @Param("ring") Ring ring,
      @Param("since") LocalDateTime since
  );
}