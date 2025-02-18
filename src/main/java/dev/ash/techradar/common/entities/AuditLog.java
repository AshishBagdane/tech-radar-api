package dev.ash.techradar.common.entities;

import dev.ash.techradar.domain.technology.util.ChangeTrackingUtil;
import dev.ash.techradar.domain.technology.util.ChangeTrackingUtil.ChangeEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Slf4j
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entityType;

    @Column(nullable = false)
    private String entityId;

    @Lob
    @Column(name = "change_data", columnDefinition = "text")
    private String changes;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 100)
    private String performedBy;

    @Column(length = 50)
    private String ipAddress;

    public Map<String, ChangeEntry> getChangesMap() {
        return ChangeTrackingUtil.deserializeChanges(this.changes);
    }

    @PostLoad
    private void postLoad() {
        log.debug("PostLoad - Changes value: {}", changes);
    }

    @PrePersist
    private void prePersist() {
        log.debug("PrePersist - Changes value: {}", changes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLog that)) {
            return false;
        }
        // Using composite natural key
        return entityType != null && entityId != null && timestamp != null &&
            entityType.equals(that.entityType) &&
            entityId.equals(that.entityId) &&
            timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
