package dev.ash.techradar.common.observability.metrics.config;

import dev.ash.techradar.common.observability.metrics.TechRadarMetrics;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;

@Configuration
@EnableAspectJAutoProxy
public class MetricsConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        // Common tags for all metrics
        registry.config().commonTags(Arrays.asList(
            Tag.of("application", "tech-radar"),
            Tag.of("environment", System.getProperty("ENVIRONMENT", "local"))
        ));

        return registry;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public TechRadarMetrics techRadarMetrics(MeterRegistry registry) {
        return new TechRadarMetrics(registry);
    }
}
