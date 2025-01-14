package dev.ash.techradar.common.observability.trace.config;

import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collection;

@Configuration
public class TracingConfig {

    private final Environment environment;

    public TracingConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenTelemetrySdk openTelemetry() {
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
            .setSampler(configureSampler())
            .addSpanProcessor(BatchSpanProcessor.builder(
                    // Configure your exporter here (Jaeger, Zipkin, etc.)
                    new CustomSpanExporter())
                                  .build())
            .build();

        return OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build();
    }

    private Sampler configureSampler() {
        String env = environment.getProperty("ENVIRONMENT", "local");
        // Adjust sampling rates based on environment
        return switch (env) {
            case "production" -> Sampler.traceIdRatioBased(0.1); // 10% sampling
            case "staging" -> Sampler.traceIdRatioBased(0.5);    // 50% sampling
            default -> Sampler.alwaysOn();                       // 100% sampling for local/dev
        };
    }
}

class CustomSpanExporter implements io.opentelemetry.sdk.trace.export.SpanExporter {

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        // Here you would implement the actual export logic
        // For example, sending to Jaeger, Zipkin, etc.
        spans.forEach(span -> {
            // Log span details for now
            System.out.println("Exporting span: " + span.getName());
            System.out.println("Trace ID: " + span.getTraceId());
            System.out.println("Span ID: " + span.getSpanId());
            System.out.println("Parent Span ID: " + span.getParentSpanId());
            System.out.println("Start Time: " + span.getStartEpochNanos());
            System.out.println("End Time: " + span.getEndEpochNanos());
        });

        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
    }
}
