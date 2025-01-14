package dev.ash.techradar.common.observability.health.indicators;

import dev.ash.techradar.common.observability.health.constants.HealthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthIndicator.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            // Execute a simple query to check database connectivity
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            // Check database connection pool
            Integer activeConnections = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM pg_stat_activity WHERE state = 'active'",
                Integer.class
            );

            return Health.up()
                .withDetail("component", HealthConstants.DATABASE_COMPONENT)
                .withDetail(HealthConstants.STATUS_KEY, HealthConstants.STATUS_UP)
                .withDetail("activeConnections", activeConnections)
                .build();

        } catch (DataAccessException e) {
            logger.error("Database health check failed", e);
            return Health.down()
                .withDetail("component", HealthConstants.DATABASE_COMPONENT)
                .withDetail(HealthConstants.STATUS_KEY, HealthConstants.STATUS_DOWN)
                .withDetail(HealthConstants.ERROR_KEY, e.getMessage())
                .build();
        }
    }
}
