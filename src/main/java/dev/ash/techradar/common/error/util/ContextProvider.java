package dev.ash.techradar.common.error.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for providing context information for errors.
 */
@UtilityClass
public class ContextProvider {

    /**
     * Gets the current request path.
     */
    public String getCurrentPath() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(ServletRequestAttributes.class::isInstance)
            .map(ServletRequestAttributes.class::cast)
            .map(ServletRequestAttributes::getRequest)
            .map(HttpServletRequest::getRequestURI)
            .orElse("unknown");
    }

    /**
     * Gets the current request method.
     */
    public String getCurrentMethod() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(ServletRequestAttributes.class::isInstance)
            .map(ServletRequestAttributes.class::cast)
            .map(ServletRequestAttributes::getRequest)
            .map(HttpServletRequest::getMethod)
            .orElse("unknown");
    }

    /**
     * Collects current request context information.
     */
    public Map<String, String> getRequestContext() {
        Map<String, String> context = new HashMap<>();

        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(ServletRequestAttributes.class::isInstance)
            .map(ServletRequestAttributes.class::cast)
            .map(ServletRequestAttributes::getRequest)
            .ifPresent(request -> {
                context.put("path", request.getRequestURI());
                context.put("method", request.getMethod());
                context.put("remoteAddr", request.getRemoteAddr());
                context.put("userAgent", request.getHeader("User-Agent"));
            });

        return context;
    }
}
