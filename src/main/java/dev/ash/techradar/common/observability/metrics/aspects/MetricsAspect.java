package dev.ash.techradar.common.observability.metrics.aspects;

import dev.ash.techradar.common.observability.metrics.TechRadarMetrics;
import io.micrometer.core.annotation.Timed;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
public class MetricsAspect {

    private final TechRadarMetrics metrics;

    public MetricsAspect(TechRadarMetrics metrics) {
        this.metrics = metrics;
    }

    @Around("@annotation(timed)")
    public Object timeMethod(ProceedingJoinPoint joinPoint, Timed timed) throws Throwable {
        Instant start = Instant.now();
        try {
            return joinPoint.proceed();
        } finally {
            Instant finish = Instant.now();
            long timeElapsed = finish.toEpochMilli() - start.toEpochMilli();
            metrics.recordApiLatency(
                joinPoint.getSignature().getName(),
                "method",
                200, // You might want to extract the actual status code
                timeElapsed
            );
        }
    }
}
