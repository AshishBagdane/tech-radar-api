package dev.ash.techradar.common.events;

import dev.ash.techradar.common.entities.AuditLog;
import dev.ash.techradar.common.repositories.AuditLogRepository;
import dev.ash.techradar.domain.technology.events.TechnologyChangeEvent;
import dev.ash.techradar.domain.technology.util.ChangeTrackingUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventHandlers {

    private final AuditLogRepository auditLogRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTechnologyChange(TechnologyChangeEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(event.getChangeType().name());
        auditLog.setEntityType("Technology");
        auditLog.setEntityId(event.getTechnology().getId().toString());
        auditLog.setTimestamp(event.getChangeDate());
        auditLog.setChanges(ChangeTrackingUtil.serializeChanges(
            convertPropertyChangesToMap(event)));

        // Add user and IP information
        setAuditContext(auditLog);

        // Add debug logging for changes
        log.debug("Changes before serialization: {}", convertPropertyChangesToMap(event));

        // Debug BEFORE save
        log.debug("About to save audit log. Changes type: {}, value: {}",
                  auditLog.getChanges().getClass().getName(),
                  auditLog.getChanges());

        String serializedChanges = ChangeTrackingUtil.serializeChanges(convertPropertyChangesToMap(event));
        // Add debug logging for serialized changes
        log.debug("Serialized changes: {}", serializedChanges);

        auditLog.setChanges(serializedChanges);
        AuditLog savedLog = auditLogRepository.save(auditLog);

        log.debug("Created audit log entry for technology change: {}", event.getChangeType());

        // Debug AFTER save
        log.debug("Saved audit log. Changes type: {}, value: {}",
                  savedLog.getChanges().getClass().getName(),
                  savedLog.getChanges());
    }

    @EventListener
    public void handleTechnologyAccessEvent(TechnologyAccessEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("ACCESS");
        auditLog.setEntityType("Technology");
        auditLog.setEntityId(event.getTechnologyId().toString());
        auditLog.setTimestamp(LocalDateTime.now());

        setAuditContext(auditLog);

        auditLogRepository.save(auditLog);
        log.debug("Created audit log entry for technology access: {}",
                  event.getTechnologyId());
    }

    @EventListener
    public void handleBatchOperationEvent(TechnologyBatchOperationEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("BATCH_" + event.getOperationType());
        auditLog.setEntityType("Technology");
        auditLog.setEntityId("BATCH");
        auditLog.setTimestamp(LocalDateTime.now());

        // Create a map with operation details
        Map<String, Object[]> changes = new HashMap<>();
        changes.put("operationType", new Object[]{null, event.getOperationType()});
        changes.put("affectedCount", new Object[]{null, event.getAffectedCount()});
        changes.put("operationTime", new Object[]{null, event.getOperationTime()});

        // Add operation details as a serialized change
        if (event.getOperationDetails() != null) {
            event.getOperationDetails().forEach((key, value) ->
                                                    changes.put("detail." + key, new Object[]{null, value}));
        }

        auditLog.setChanges(ChangeTrackingUtil.serializeChanges(changes));

        setAuditContext(auditLog);

        auditLogRepository.save(auditLog);
        log.debug("Created audit log entry for batch operation: {}",
                  event.getOperationType());
    }

    private void setAuditContext(AuditLog auditLog) {
        // Get current request details
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // Set IP Address - handle potential proxy scenarios
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }

            auditLog.setIpAddress(ipAddress);

            // Set performed_by - assuming you have a security context
            auditLog.setPerformedBy("anonymous");
        } else {
            // Fallback values for non-web requests
            auditLog.setIpAddress("system");
            auditLog.setPerformedBy("system");
        }

        log.debug("Set audit context - IP: {}, User: {}",
                  auditLog.getIpAddress(),
                  auditLog.getPerformedBy());
    }

    private Map<String, Object[]> convertPropertyChangesToMap(TechnologyChangeEvent event) {
        return event.getChanges().entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> new Object[]{
                    e.getValue().getPreviousValue(),
                    e.getValue().getNewValue()
                }
            ));
    }
}
