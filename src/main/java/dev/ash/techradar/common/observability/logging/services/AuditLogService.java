package dev.ash.techradar.common.observability.logging.services;

import dev.ash.techradar.common.entities.AuditLog;
import dev.ash.techradar.common.repositories.AuditLogRepository;
import dev.ash.techradar.domain.technology.util.ChangeTrackingUtil;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLogSummary> getChangesByField(String fieldName, String entityType) {
        List<AuditLog> logs = auditLogRepository.findChangesByField(fieldName, entityType);
        return logs.stream()
            .map(this::createChangeSummary)
            .collect(Collectors.toList());
    }

    public List<AuditLogSummary> getChangesByValue(String value, String entityType) {
        List<AuditLog> logs = auditLogRepository.findChangesByValue(value, entityType);
        return logs.stream()
            .map(this::createChangeSummary)
            .collect(Collectors.toList());
    }

    private AuditLogSummary createChangeSummary(AuditLog log) {
        Map<String, ChangeTrackingUtil.ChangeEntry> changes = log.getChangesMap();
        return AuditLogSummary.builder()
            .id(log.getId())
            .action(log.getAction())
            .entityType(log.getEntityType())
            .entityId(log.getEntityId())
            .timestamp(log.getTimestamp())
            .performedBy(log.getPerformedBy())
            .changes(changes)
            .build();
    }

    @Data
    @Builder
    public static class AuditLogSummary {

        private UUID id;

        private String action;

        private String entityType;

        private String entityId;

        private LocalDateTime timestamp;

        private String performedBy;

        private Map<String, ChangeTrackingUtil.ChangeEntry> changes;
    }
}
