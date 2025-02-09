package ru.otus.hw.actuators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class dBHealthIndicator implements HealthIndicator {

    private final JdbcOperations jdbcTemplate;

    @Override
    public Health health() {
        try {
            jdbcTemplate.execute("select 1 from dual");
            return Health.up().build();
        } catch (Exception ex) {
            return Health.down().withDetail("Error message", ex.getMessage()).build();
        }
    }
}