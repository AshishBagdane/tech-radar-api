package dev.ash.techradar.common.observability.health.indicators;

import dev.ash.techradar.common.observability.health.constants.HealthConstants;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class TechnologyDataHealthIndicator implements HealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(TechnologyDataHealthIndicator.class);

    private final JdbcTemplate jdbcTemplate;

    public TechnologyDataHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            Map<String, Object> details = new HashMap<>();
            Health.Builder builder = new Health.Builder();

            // Count total technologies
            Integer totalTechnologies = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM technologies",
                Integer.class
            );

            // Get technologies by quadrant
            Map<String, Integer> technologiesByQuadrant = getTechnologiesByQuadrant();

            // Get technologies by ring
            Map<String, Integer> technologiesByRing = getTechnologiesByRing();

            // Get latest update timestamp
            LocalDateTime lastUpdated = jdbcTemplate.queryForObject(
                "SELECT MAX(updated_at) FROM technologies",
                LocalDateTime.class
            );

            // Check orphaned records
            Integer orphanedMetadata = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM technology_metadata tm " +
                    "LEFT JOIN technologies t ON tm.technology_id = t.id " +
                    "WHERE t.id IS NULL",
                Integer.class
            );

            // Build health details
            details.put("totalTechnologies", totalTechnologies);
            details.put("distributionByQuadrant", technologiesByQuadrant);
            details.put("distributionByRing", technologiesByRing);
            details.put("lastUpdated", lastUpdated);
            details.put("orphanedMetadataRecords", orphanedMetadata);

            // Determine health status
            if (isHealthy(totalTechnologies, technologiesByQuadrant, technologiesByRing, orphanedMetadata)) {
                builder.up();
            } else {
                builder.down();
                details.put("issues", collectHealthIssues(totalTechnologies, technologiesByQuadrant, technologiesByRing,
                                                          orphanedMetadata));
            }

            return builder
                .withDetail("component", HealthConstants.TECHNOLOGY_DATA_COMPONENT)
                .withDetails(details)
                .build();

        } catch (Exception e) {
            logger.error("Error checking technology data health", e);
            return Health.down()
                .withDetail("component", HealthConstants.TECHNOLOGY_DATA_COMPONENT)
                .withDetail("error", e.getMessage())
                .build();
        }
    }

    private Map<String, Integer> getTechnologiesByQuadrant() {
        Map<String, Integer> distribution = new HashMap<>();
        for (Quadrant quadrant : Quadrant.values()) {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM technologies WHERE quadrant = ?",
                Integer.class,
                quadrant.name()
            );
            distribution.put(quadrant.name(), count);
        }
        return distribution;
    }

    private Map<String, Integer> getTechnologiesByRing() {
        Map<String, Integer> distribution = new HashMap<>();
        for (Ring ring : Ring.values()) {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM technologies WHERE ring = ?",
                Integer.class,
                ring.name()
            );
            distribution.put(ring.name(), count);
        }
        return distribution;
    }

    private boolean isHealthy(Integer totalTechnologies,
                              Map<String, Integer> byQuadrant,
                              Map<String, Integer> byRing,
                              Integer orphanedMetadata) {
        if (totalTechnologies < HealthConstants.MIN_TECHNOLOGIES_COUNT) {
            return false;
        }

        // Check if sum of technologies by quadrant matches total
        if (byQuadrant.values().stream().mapToInt(Integer::intValue).sum() != totalTechnologies) {
            return false;
        }

        // Check if sum of technologies by ring matches total
        if (byRing.values().stream().mapToInt(Integer::intValue).sum() != totalTechnologies) {
            return false;
        }

        // Check for orphaned metadata
        if (orphanedMetadata > 0) {
            return false;
        }

        return true;
    }

    private Map<String, String> collectHealthIssues(Integer totalTechnologies,
                                                    Map<String, Integer> byQuadrant,
                                                    Map<String, Integer> byRing,
                                                    Integer orphanedMetadata) {
        Map<String, String> issues = new HashMap<>();

        if (totalTechnologies < HealthConstants.MIN_TECHNOLOGIES_COUNT) {
            issues.put("insufficientData",
                       String.format("Total technologies (%d) is below minimum required (%d)",
                                     totalTechnologies, HealthConstants.MIN_TECHNOLOGIES_COUNT));
        }

        int quadrantSum = byQuadrant.values().stream().mapToInt(Integer::intValue).sum();
        if (quadrantSum != totalTechnologies) {
            issues.put("quadrantMismatch",
                       String.format("Sum of technologies by quadrant (%d) doesn't match total (%d)",
                                     quadrantSum, totalTechnologies));
        }

        int ringSum = byRing.values().stream().mapToInt(Integer::intValue).sum();
        if (ringSum != totalTechnologies) {
            issues.put("ringMismatch",
                       String.format("Sum of technologies by ring (%d) doesn't match total (%d)",
                                     ringSum, totalTechnologies));
        }

        if (orphanedMetadata > 0) {
            issues.put("orphanedMetadata",
                       String.format("Found %d orphaned metadata records", orphanedMetadata));
        }

        return issues;
    }
}
