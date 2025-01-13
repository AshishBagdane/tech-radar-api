package dev.ash.techradar.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
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

  @Column(columnDefinition = "jsonb")
  private String changes;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(length = 100)
  private String performedBy;

  @Column(length = 50)
  private String ipAddress;

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