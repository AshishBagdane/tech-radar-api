package dev.ash.techradar.common.error.handler;

import dev.ash.techradar.common.error.core.ErrorMessage;
import dev.ash.techradar.common.error.exception.base.AbstractTechRadarException;
import dev.ash.techradar.common.error.metrics.ErrorMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom error attributes handler for the Tech Radar application.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TechRadarErrorAttributes extends DefaultErrorAttributes {

    private final ErrorResponseBuilder errorResponseBuilder;

    private final ErrorMetrics errorMetrics;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                  ErrorAttributeOptions options) {
        Throwable error = getError(webRequest);

        // Record metric
        errorMetrics.incrementErrorCount(error.getClass().getSimpleName());

        // Handle different types of exceptions
        if (error instanceof AbstractTechRadarException) {
            return handleTechRadarException((AbstractTechRadarException) error);
        } else if (error instanceof MethodArgumentNotValidException) {
            return handleValidationException((MethodArgumentNotValidException) error);
        } else {
            return handleUnexpectedException(error);
        }
    }

    private Map<String, Object> handleTechRadarException(AbstractTechRadarException ex) {
        ErrorMessage errorMessage = ex.getErrorMessage();
        log.error("Tech Radar exception occurred: {}", errorMessage, ex);
        return errorResponseBuilder.buildErrorResponse(errorMessage);
    }

    private Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> fieldError.getDefaultMessage() == null ?
                    "Invalid value" : fieldError.getDefaultMessage(),
                (error1, error2) -> error1
            ));

        log.error("Validation exception occurred: {}", validationErrors, ex);

        return errorResponseBuilder.buildErrorResponse(
            ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Validation failed for " + validationErrors.size() + " field(s)")
                .details(Map.of("validationErrors", validationErrors))
                .build()
        );
    }

    private Map<String, Object> handleUnexpectedException(Throwable error) {
        log.error("Unexpected exception occurred", error);

        return errorResponseBuilder.buildErrorResponse(
            ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .build()
        );
    }
}
