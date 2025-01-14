package dev.ash.techradar.common.observability.health.indicators;

import dev.ash.techradar.common.observability.health.constants.HealthConstants;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    private final RuntimeMXBean runtimeBean;

    private final Instant startTime;

    public ApplicationHealthIndicator() {
        this.runtimeBean = ManagementFactory.getRuntimeMXBean();
        this.startTime = Instant.now();
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();

        // Add uptime information
        long uptimeMillis = runtimeBean.getUptime();
        Duration uptime = Duration.ofMillis(uptimeMillis);
        details.put("uptime", formatDuration(uptime));

        // Add JVM information
        details.put("jvm", Map.of(
            "version", runtimeBean.getVmVersion(),
            "vendor", runtimeBean.getVmVendor(),
            "name", runtimeBean.getVmName()
        ));

        // Add start time
        details.put("startTime", startTime.toString());

        // Add system properties
        details.put("system", Map.of(
            "os", System.getProperty("os.name"),
            "arch", System.getProperty("os.arch"),
            "javaVersion", System.getProperty("java.version"),
            "availableProcessors", Runtime.getRuntime().availableProcessors()
        ));

        return Health.up()
            .withDetail("component", HealthConstants.APPLICATION_COMPONENT)
            .withDetails(details)
            .build();
    }

    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();

        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(days).append("d ");
        }
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        result.append(seconds).append("s");

        return result.toString();
    }
}
