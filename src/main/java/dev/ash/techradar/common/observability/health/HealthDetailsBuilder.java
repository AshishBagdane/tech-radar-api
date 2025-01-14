package dev.ash.techradar.common.observability.health;

import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class HealthDetailsBuilder {

    public HealthDetails createDetails(String component, Status status) {
        return HealthDetails.builder()
            .component(component)
            .status(status.getCode())
            .timestamp(LocalDateTime.now())
            .build();
    }

    public HealthDetails createDetails(String component, Status status, String message) {
        return HealthDetails.builder()
            .component(component)
            .status(status.getCode())
            .timestamp(LocalDateTime.now())
            .message(message)
            .build();
    }

    public HealthDetails createDetails(String component, Status status, Map<String, Object> metrics) {
        return HealthDetails.builder()
            .component(component)
            .status(status.getCode())
            .timestamp(LocalDateTime.now())
            .metrics(new HashMap<>(metrics))
            .build();
    }

    public HealthDetails createErrorDetails(String component, String errorMessage, Map<String, String> errors) {
        return HealthDetails.builder()
            .component(component)
            .status(Status.DOWN.getCode())
            .timestamp(LocalDateTime.now())
            .message(errorMessage)
            .errors(errors)
            .build();
    }
}
