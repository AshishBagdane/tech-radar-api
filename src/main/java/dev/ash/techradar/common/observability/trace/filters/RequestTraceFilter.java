package dev.ash.techradar.common.observability.trace.filters;

import dev.ash.techradar.common.observability.trace.constants.TraceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestTraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String correlationId = extractOrGenerateCorrelationId(request);

        try {
            MDC.put(TraceConstants.CORRELATION_ID_KEY, correlationId);
            response.setHeader(TraceConstants.CORRELATION_ID_HEADER, correlationId);

            // Add business context to MDC
            addBusinessContext(request);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String extractOrGenerateCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(TraceConstants.CORRELATION_ID_HEADER);
        return correlationId != null ? correlationId : UUID.randomUUID().toString();
    }

    private void addBusinessContext(HttpServletRequest request) {
        // Add business-specific context to MDC
        String quadrant = request.getParameter("quadrant");
        if (quadrant != null) {
            MDC.put("quadrant", quadrant);
        }

        String ring = request.getParameter("ring");
        if (ring != null) {
            MDC.put("ring", ring);
        }
    }
}
