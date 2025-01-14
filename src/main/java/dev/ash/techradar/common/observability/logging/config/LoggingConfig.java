package dev.ash.techradar.common.observability.logging.config;

import ch.qos.logback.classic.LoggerContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class LoggingConfig {

    @Value("${spring.application.name:tech-radar}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @PostConstruct
    public void init() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Set application name and profile as default MDC values
        loggerContext.putProperty("appName", applicationName);
        loggerContext.putProperty("profile", activeProfile);
    }
}
