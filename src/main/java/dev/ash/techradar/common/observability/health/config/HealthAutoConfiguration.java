package dev.ash.techradar.common.observability.health.config;

import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(HealthProperties.class)
@Import(HealthContributorAutoConfiguration.class)
public class HealthAutoConfiguration {

    @Bean
    @Primary
    public HealthContributor techRadarHealthContributor(
        CompositeHealthContributorImpl healthAggregator) {
        return healthAggregator;
    }
}
