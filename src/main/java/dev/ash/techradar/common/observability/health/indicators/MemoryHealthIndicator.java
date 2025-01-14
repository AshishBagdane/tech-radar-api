package dev.ash.techradar.common.observability.health.indicators;

import dev.ash.techradar.common.observability.health.constants.HealthConstants;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Component
public class MemoryHealthIndicator implements HealthIndicator {

    private final MemoryMXBean memoryBean;

    public MemoryHealthIndicator() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }

    @Override
    public Health health() {
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();

        double heapUsagePercent = calculateUsagePercentage(heapMemory);

        Health.Builder builder = new Health.Builder();

        if (heapUsagePercent >= HealthConstants.MEMORY_CRITICAL_THRESHOLD) {
            builder.down();
        } else if (heapUsagePercent >= HealthConstants.MEMORY_WARN_THRESHOLD) {
            builder.status(HealthConstants.STATUS_DEGRADED);
        } else {
            builder.up();
        }

        return builder
            .withDetail("component", HealthConstants.MEMORY_COMPONENT)
            .withDetail("heap", getMemoryDetails(heapMemory))
            .withDetail("nonHeap", getMemoryDetails(nonHeapMemory))
            .build();
    }

    private double calculateUsagePercentage(MemoryUsage memory) {
        if (memory.getMax() > 0) {
            return (double) memory.getUsed() / memory.getMax() * 100;
        }
        return 0.0;
    }

    private java.util.Map<String, Object> getMemoryDetails(MemoryUsage memory) {
        java.util.Map<String, Object> details = new java.util.HashMap<>();
        details.put(HealthConstants.USED_KEY, formatBytes(memory.getUsed()));
        details.put(HealthConstants.TOTAL_KEY, formatBytes(memory.getMax()));
        details.put(HealthConstants.FREE_KEY, formatBytes(memory.getMax() - memory.getUsed()));
        details.put(HealthConstants.PERCENTAGE_KEY, String.format("%.2f%%", calculateUsagePercentage(memory)));
        return details;
    }

    private String formatBytes(long bytes) {
        if (bytes < 0) {
            return "N/A";
        }
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        }
        if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        }
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
