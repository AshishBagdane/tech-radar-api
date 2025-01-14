package dev.ash.techradar;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(info = @Info(
    version = "2025.1.0-SNAPSHOT",
    title = "${spring.application.name}",
    summary = "APIs for Tech Radar"
))
@EntityScan(basePackages = {"dev.ash.techradar"})
@EnableJpaRepositories(basePackages = {"dev.ash.techradar"})
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
public class TechRadarApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechRadarApplication.class, args);
    }

}
