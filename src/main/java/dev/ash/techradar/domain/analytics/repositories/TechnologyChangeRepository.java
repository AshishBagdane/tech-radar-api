package dev.ash.techradar.domain.analytics.repositories;

import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TechnologyChangeRepository extends JpaRepository<TechnologyChange, UUID>,
    JpaSpecificationExecutor<TechnologyChange> {
    // All queries will be handled through specifications
}
