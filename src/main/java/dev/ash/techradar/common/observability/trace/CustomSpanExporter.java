package dev.ash.techradar.common.observability.trace;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class CustomSpanExporter implements SpanExporter {

    private static final Logger logger = LoggerFactory.getLogger(CustomSpanExporter.class);

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        spans.forEach(span -> {
            logger.debug("Exporting span: {}", span.getName());
            logger.trace("Span details - TraceId: {}, SpanId: {}, ParentSpanId: {}, StartTime: {}, EndTime: {}",
                         span.getTraceId(),
                         span.getSpanId(),
                         span.getParentSpanId(),
                         span.getStartEpochNanos(),
                         span.getEndEpochNanos()
            );
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
