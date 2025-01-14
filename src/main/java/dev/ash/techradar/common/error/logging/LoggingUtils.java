package dev.ash.techradar.common.error.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Utility methods for error logging.
 */
@Component
@RequiredArgsConstructor
public class LoggingUtils {

    private final ObjectMapper objectMapper;

    private static final Set<String> SENSITIVE_FIELDS = new HashSet<>(Arrays.asList(
        "password", "token", "secret", "authorization", "key"
    ));

    /**
     * Gets the current trace ID from MDC or generates a new one.
     */
    public String getCurrentTraceId() {
        return Optional.ofNullable(MDC.get("traceId"))
            .orElseGet(() -> UUID.randomUUID().toString());
    }

    /**
     * Gets the current request path.
     */
    public String getCurrentRequestPath() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(ServletRequestAttributes.class::isInstance)
            .map(ServletRequestAttributes.class::cast)
            .map(ServletRequestAttributes::getRequest)
            .map(request -> request.getRequestURI())
            .orElse("unknown");
    }

    /**
     * Masks sensitive data in objects before logging.
     */
    @SuppressWarnings("unchecked")
    public Object maskSensitiveData(Object data) {
        if (data == null) {
            return null;
        }

        try {
            // Convert to map for processing
            Map<String, Object> dataMap;
            if (data instanceof Map) {
                dataMap = (Map<String, Object>) data;
            } else {
                String json = objectMapper.writeValueAsString(data);
                dataMap = objectMapper.readValue(json, Map.class);
            }

            // Mask sensitive fields
            maskSensitiveFieldsInMap(dataMap);

            return dataMap;
        } catch (JsonProcessingException e) {
            // If can't process as JSON, return toString
            return data.toString();
        }
    }

    /**
     * Recursively masks sensitive fields in a map.
     */
    private void maskSensitiveFieldsInMap(Map<String, Object> map) {
        map.forEach((key, value) -> {
            if (SENSITIVE_FIELDS.stream().anyMatch(key.toLowerCase()::contains)) {
                map.put(key, "********");
            } else if (value instanceof Map) {
                maskSensitiveFieldsInMap((Map<String, Object>) value);
            }
        });
    }

    /**
     * Formats error context for logging.
     */
    public String formatErrorContext(Map<String, Object> context) {
        try {
            return objectMapper.writeValueAsString(maskSensitiveData(context));
        } catch (JsonProcessingException e) {
            return context.toString();
        }
    }

    /**
     * Adds a field to list of sensitive fields.
     */
    public void addSensitiveField(String fieldName) {
        SENSITIVE_FIELDS.add(fieldName.toLowerCase());
    }
}
