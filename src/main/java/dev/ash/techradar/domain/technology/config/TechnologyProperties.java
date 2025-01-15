package dev.ash.techradar.domain.technology.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tech-radar.technology")
public class TechnologyProperties {

    private ValidationProperties validation = new ValidationProperties();

    @Data
    public static class ValidationProperties {

        private int maxNameLength = 100;

        private int maxDescriptionLength = 500;

        private int maxMetadataEntries = 20;

        private int maxMetadataKeyLength = 50;

        private int maxMetadataValueLength = 200;
    }
}
