package dev.ash.techradar.common.error.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for metrics in the Tech Radar application.
 */
@Configuration
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public ErrorMetrics errorMetrics(MeterRegistry registry) {
        return new ErrorMetrics(registry);
    }

    @Bean
    public MetricsRegistry metricsRegistry(MeterRegistry registry) {
        return new MetricsRegistry(registry);
    }
}
