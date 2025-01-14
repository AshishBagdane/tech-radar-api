package dev.ash.techradar.common.observability.metrics;

import dev.ash.techradar.common.observability.metrics.constants.MetricsConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TechRadarMetrics {

    private final MeterRegistry registry;

    public TechRadarMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordTechnologyCreation(String quadrant, String ring) {
        Counter.builder(MetricsConstants.Metrics.TECHNOLOGY_CREATED)
            .tag(MetricsConstants.Tags.QUADRANT, quadrant)
            .tag(MetricsConstants.Tags.RING, ring)
            .register(registry)
            .increment();
    }

    public void recordTechnologyUpdate(String quadrant, String ring) {
        Counter.builder(MetricsConstants.Metrics.TECHNOLOGY_UPDATED)
            .tag(MetricsConstants.Tags.QUADRANT, quadrant)
            .tag(MetricsConstants.Tags.RING, ring)
            .register(registry)
            .increment();
    }

    public void recordApiLatency(String endpoint, String method, int statusCode, long latencyMs) {
        Timer.builder(MetricsConstants.Metrics.API_LATENCY)
            .tag(MetricsConstants.Tags.ENDPOINT, endpoint)
            .tag(MetricsConstants.Tags.METHOD, method)
            .tag(MetricsConstants.Tags.STATUS, String.valueOf(statusCode))
            .register(registry)
            .record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordError(String errorType) {
        Counter.builder(MetricsConstants.Metrics.ERROR_COUNT)
            .tag(MetricsConstants.Tags.ERROR_TYPE, errorType)
            .register(registry)
            .increment();
    }
}
