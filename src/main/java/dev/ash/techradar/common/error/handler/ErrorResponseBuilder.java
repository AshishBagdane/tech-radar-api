package dev.ash.techradar.common.error.handler;

import dev.ash.techradar.common.error.core.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Builder for creating consistent error responses.
 */
@Component
public class ErrorResponseBuilder {

    /**
     * Builds a standardized error response map from an ErrorMessage.
     */
    public Map<String, Object> buildErrorResponse(ErrorMessage errorMessage) {
        Map<String, Object> errorAttributes = new HashMap<>();

        // Add standard error attributes
        errorAttributes.put("timestamp", errorMessage.getTimestamp());
        errorAttributes.put("status", errorMessage.getStatus());
        errorAttributes.put("error", errorMessage.getError());
        errorAttributes.put("message", errorMessage.getMessage());

        // Add path if not present
        errorAttributes.put("path", errorMessage.getPath() != null ?
            errorMessage.getPath() : getCurrentRequestPath());

        // Add trace ID if present
        if (errorMessage.getTraceId() != null) {
            errorAttributes.put("traceId", errorMessage.getTraceId());
        }

        // Add error details if present
        if (errorMessage.getDetails() != null && !errorMessage.getDetails().isEmpty()) {
            errorAttributes.put("details", errorMessage.getDetails());
        }

        return errorAttributes;
    }

    /**
     * Builds a simple error response with minimal information.
     */
    public Map<String, Object> buildSimpleErrorResponse(String message, int status) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("status", status);
        errorAttributes.put("message", message);
        errorAttributes.put("path", getCurrentRequestPath());
        return errorAttributes;
    }

    /**
     * Retrieves the current request path.
     */
    private String getCurrentRequestPath() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(ServletRequestAttributes.class::isInstance)
            .map(ServletRequestAttributes.class::cast)
            .map(ServletRequestAttributes::getRequest)
            .map(HttpServletRequest::getRequestURI)
            .orElse("unknown");
    }
}
