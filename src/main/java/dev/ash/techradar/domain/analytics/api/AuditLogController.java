package dev.ash.techradar.domain.analytics.api;

import dev.ash.techradar.common.observability.logging.services.AuditLogService;
import dev.ash.techradar.common.observability.logging.services.AuditLogService.AuditLogSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/changes/{entityType}")
    public List<AuditLogSummary> getChangesByField(
        @PathVariable String entityType,
        @RequestParam(required = false) String field,
        @RequestParam(required = false) String value) {

        if (field != null) {
            return auditLogService.getChangesByField(field, entityType);
        } else if (value != null) {
            return auditLogService.getChangesByValue(value, entityType);
        }

        throw new IllegalArgumentException("Either field or value parameter is required");
    }
}
