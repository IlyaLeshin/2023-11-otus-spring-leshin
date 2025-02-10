package ru.otus.hw.actuators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.CommonLibraryDbService;

@Component
@AllArgsConstructor
public class DBHealthIndicator implements HealthIndicator {

    private final CommonLibraryDbService commonLibraryDbService;

    @Override
    public Health health() {
        if (commonLibraryDbService.checkingAccessToDb()) {
            return Health.up().withDetail("Message", "The database is available").build();
        }
        return Health.down().withDetail("Message", "The database is unavailable").build();
    }
}