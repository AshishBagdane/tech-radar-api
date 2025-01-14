package dev.ash.techradar.common.observability.health.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tech-radar.health")
public class HealthProperties {

    private boolean showDetails = true;

    private double memoryThresholdPercent = 80.0;

    private int minTechnologiesCount = 1;
}
