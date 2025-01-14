package dev.ash.techradar.common.observability.logging.filters;

import dev.ash.techradar.common.observability.logging.utils.LoggingUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    private static final int MAX_PAYLOAD_LENGTH = 10000;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Wrap request and response for logging
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String requestId = UUID.randomUUID().toString();
        Instant startTime = Instant.now();

        try {
            // Set up MDC context
            setupLoggingContext(requestWrapper, requestId);

            // Log request
            logRequest(requestWrapper);

            // Execute the rest of the filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);

            // Log response
            logResponse(responseWrapper, startTime);

        } finally {
            // Copy content to response
            responseWrapper.copyBodyToResponse();
            // Clear MDC context
            LoggingUtils.clearMdc();
        }
    }

    private void setupLoggingContext(HttpServletRequest request, String requestId) {
        LoggingUtils.addRequestContext(
            request.getRequestURI(),
            request.getMethod()
        );

        // Add business context if available
        LoggingUtils.addBusinessContext(
            request.getParameter("quadrant"),
            request.getParameter("ring"),
            request.getParameter("technologyId")
        );
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String payload = getRequestPayload(request);
        logger.info("Incoming Request: method={}, uri={}, payload={}, headers={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    payload,
                    extractHeaders(request)
        );
    }

    private void logResponse(ContentCachingResponseWrapper response, Instant startTime) {
        Duration duration = Duration.between(startTime, Instant.now());
        String payload = getResponsePayload(response);

        logger.info("Outgoing Response: status={}, payload={}, duration={}ms",
                    response.getStatus(),
                    payload,
                    duration.toMillis()
        );
    }

    private String getRequestPayload(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length == 0) {
            return "";
        }
        return truncatePayload(new String(content));
    }

    private String getResponsePayload(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length == 0) {
            return "";
        }
        return truncatePayload(new String(content));
    }

    private String truncatePayload(String payload) {
        if (payload.length() <= MAX_PAYLOAD_LENGTH) {
            return payload;
        }
        return payload.substring(0, MAX_PAYLOAD_LENGTH) + "...";
    }

    private String extractHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                                                                   headers.append(headerName).append("=")
                                                                       .append(request.getHeader(headerName))
                                                                       .append(", ")
        );
        return headers.toString();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/actuator/") ||
            path.contains("/health") ||
            path.contains("/metrics");
    }
}
