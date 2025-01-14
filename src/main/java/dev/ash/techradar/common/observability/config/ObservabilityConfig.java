package dev.ash.techradar.common.observability.config;

import dev.ash.techradar.common.observability.health.config.HealthConfig;
import dev.ash.techradar.common.observability.logging.config.LoggingConfig;
import dev.ash.techradar.common.observability.metrics.config.MetricsConfig;
import dev.ash.techradar.common.observability.trace.config.TracingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Import({
    LoggingConfig.class,
    MetricsConfig.class,
    TracingConfig.class,
    HealthConfig.class
})
public class ObservabilityConfig {
    // This class serves as an aggregator for all observability configurations
}
