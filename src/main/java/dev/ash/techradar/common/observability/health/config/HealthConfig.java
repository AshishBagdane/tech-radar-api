package dev.ash.techradar.common.observability.health.config;

import dev.ash.techradar.common.observability.health.indicators.ApplicationHealthIndicator;
import dev.ash.techradar.common.observability.health.indicators.DatabaseHealthIndicator;
import dev.ash.techradar.common.observability.health.indicators.MemoryHealthIndicator;
import dev.ash.techradar.common.observability.health.indicators.TechnologyDataHealthIndicator;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class HealthConfig {

    @Bean
    public CompositeHealthContributor techRadarHealth(
        DatabaseHealthIndicator databaseHealth,
        ApplicationHealthIndicator applicationHealth,
        TechnologyDataHealthIndicator technologyDataHealth,
        MemoryHealthIndicator memoryHealth) {

        Map<String, HealthContributor> contributors = new LinkedHashMap<>();
        contributors.put("database", databaseHealth);
        contributors.put("application", applicationHealth);
        contributors.put("technologyData", technologyDataHealth);
        contributors.put("memory", memoryHealth);

        return CompositeHealthContributor.fromMap(contributors);
    }
}
