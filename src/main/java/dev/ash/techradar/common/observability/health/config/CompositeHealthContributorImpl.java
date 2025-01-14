package dev.ash.techradar.common.observability.health.config;

import dev.ash.techradar.common.observability.health.indicators.ApplicationHealthIndicator;
import dev.ash.techradar.common.observability.health.indicators.DatabaseHealthIndicator;
import dev.ash.techradar.common.observability.health.indicators.MemoryHealthIndicator;
import dev.ash.techradar.common.observability.health.indicators.TechnologyDataHealthIndicator;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CompositeHealthContributorImpl implements CompositeHealthContributor {

    private final Map<String, HealthContributor> contributors;

    public CompositeHealthContributorImpl(
        DatabaseHealthIndicator databaseHealth,
        ApplicationHealthIndicator applicationHealth,
        MemoryHealthIndicator memoryHealth,
        TechnologyDataHealthIndicator technologyDataHealth) {

        contributors = new LinkedHashMap<>();
        contributors.put("database", databaseHealth);
        contributors.put("application", applicationHealth);
        contributors.put("memory", memoryHealth);
        contributors.put("technologyData", technologyDataHealth);
    }

    @Override
    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
            .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue()))
            .iterator();
    }
}
