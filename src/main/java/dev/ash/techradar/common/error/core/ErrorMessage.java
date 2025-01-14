package dev.ash.techradar.common.error.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents the standard error response structure.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private final LocalDateTime timestamp;

    private final int status;

    private final String code;

    private final String error;

    private final String message;

    private final String path;

    private final String traceId;

    private final Map<String, Object> details;

    public static ErrorMessage from(ErrorCode errorCode, ErrorContext context) {
        return ErrorMessage.builder()
            .timestamp(LocalDateTime.now())
            .status(errorCode.getHttpStatus().value())
            .code(errorCode.getCode())
            .error(errorCode.getCategory().getDescription())
            .message(errorCode.getDefaultMessage())
            .path(context.path())
            .traceId(context.traceId())
            .details(context.attributes())
            .build();
    }
}
