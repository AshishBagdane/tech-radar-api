package dev.ash.techradar.common.observability.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthDetails {

    private String component;

    private String status;

    private LocalDateTime timestamp;

    private String message;

    private Map<String, Object> metrics;

    private Map<String, String> errors;
}
