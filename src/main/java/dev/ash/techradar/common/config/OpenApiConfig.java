package dev.ash.techradar.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${springdoc.swagger-ui.path:}")
    private String serverBasePath;

    @Bean
    public OpenAPI techRadarOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                      .title("Tech Radar API")
                      .description(
                          "REST APIs for managing technology radar data, including technologies, quadrants, and rings")
                      .version("1.0.0"))
            .addServersItem(new Server()
                                .url(serverBasePath)
                                .description("Base API Path"));
    }

    @Bean
    public GroupedOpenApi technologyApi() {
        return GroupedOpenApi.builder()
            .group("1_technologies")
            .packagesToScan("dev.ash.techradar.domain.technology")
            .pathsToMatch("/api/v1/technologies/**")
            .build();
    }

    @Bean
    public GroupedOpenApi quadrantApi() {
        return GroupedOpenApi.builder()
            .group("2_quadrants")
            .packagesToScan("dev.ash.techradar.domain.quadrant")
            .pathsToMatch("/api/v1/quadrants/**")
            .build();
    }

    @Bean
    public GroupedOpenApi ringApi() {
        return GroupedOpenApi.builder()
            .group("3_rings")
            .packagesToScan("dev.ash.techradar.domain.ring")
            .pathsToMatch("/api/v1/rings/**")
            .build();
    }

    @Bean
    public GroupedOpenApi analyticsApi() {
        return GroupedOpenApi.builder()
            .group("4_analytics")
            .packagesToScan("dev.ash.techradar.domain.analytics")
            .pathsToMatch("/api/v1/metrics/**")
            .pathsToMatch("/api/v1/audit/**")
            .build();
    }

    @Bean
    public GroupedOpenApi batchApi() {
        return GroupedOpenApi.builder()
            .group("5_batch")
            .packagesToScan("dev.ash.techradar.domain.batch")
            .pathsToMatch("/api/v1/technologies/batch/**")
            .build();
    }
}
