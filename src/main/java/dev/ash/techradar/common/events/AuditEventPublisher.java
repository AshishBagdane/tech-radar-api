package dev.ash.techradar.common.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishAccess(UUID technologyId, String accessType) {
        eventPublisher.publishEvent(TechnologyAccessEvent.builder()
                                        .technologyId(technologyId)
                                        .accessType(accessType)
                                        .username(getCurrentUsername())
                                        .accessTime(LocalDateTime.now())
                                        .build());
    }

    public void publishBatchOperation(String operationType,
                                      Map<String, Object> details,
                                      int affectedCount) {
        eventPublisher.publishEvent(TechnologyBatchOperationEvent.builder()
                                        .operationType(operationType)
                                        .operationDetails(details)
                                        .operationTime(LocalDateTime.now())
                                        .affectedCount(affectedCount)
                                        .build());
    }

    private String getCurrentUsername() {
        return "user";
    }
}
