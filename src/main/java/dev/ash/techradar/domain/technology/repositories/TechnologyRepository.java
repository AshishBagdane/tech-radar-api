package dev.ash.techradar.domain.technology.repositories;

import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, UUID>,
    JpaSpecificationExecutor<Technology> {

    List<Technology> findByRing(Ring ring);

    @Query("SELECT COUNT(t) FROM Technology t WHERE t.quadrant = :quadrant")
    long countByQuadrant(@Param("quadrant") Quadrant quadrant);

    @Query("SELECT COUNT(t) FROM Technology t WHERE t.ring = :ring")
    long countByRing(@Param("ring") Ring ring);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    Optional<Technology> findByNameIgnoreCase(String name);

    int countByRingAndQuadrant(Ring ring, Quadrant quadrant);
}
