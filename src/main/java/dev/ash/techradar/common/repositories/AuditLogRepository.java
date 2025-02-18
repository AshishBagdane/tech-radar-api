package dev.ash.techradar.common.repositories;

import dev.ash.techradar.common.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    // Custom repository methods for JSON querying
    @Query(value = """
        SELECT a FROM AuditLog a
        WHERE a.changes LIKE %:fieldName%
        AND a.entityType = :entityType
        ORDER BY a.timestamp DESC
        """)
    List<AuditLog> findChangesByField(
        @Param("fieldName") String fieldName,
        @Param("entityType") String entityType);

    @Query(value = """
        SELECT a FROM AuditLog a
        WHERE a.changes LIKE %:value%
        AND a.entityType = :entityType
        ORDER BY a.timestamp DESC
        """)
    List<AuditLog> findChangesByValue(
        @Param("value") String value,
        @Param("entityType") String entityType);
}
