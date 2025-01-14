package dev.ash.techradar.common.error.handler;

import dev.ash.techradar.common.error.metrics.ErrorMetrics;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for error handling in the Tech Radar application.
 */
@Configuration
public class ErrorHandlingConfig implements WebMvcConfigurer {

    @Bean
    public ErrorAttributes errorAttributes(ErrorResponseBuilder errorResponseBuilder,
                                           ErrorMetrics errorMetrics) {
        return new TechRadarErrorAttributes(errorResponseBuilder, errorMetrics);
    }

    @Bean
    public ErrorResponseBuilder errorResponseBuilder() {
        return new ErrorResponseBuilder();
    }
}
