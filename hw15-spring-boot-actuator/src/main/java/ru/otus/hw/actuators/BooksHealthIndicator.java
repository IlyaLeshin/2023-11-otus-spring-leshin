package ru.otus.hw.actuators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookService;

@Component
@AllArgsConstructor
public class BooksHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        var booksCount = bookService.count();
        if (booksCount != 0) {
            return Health.up().withDetail("message", "%s books on the shelves".formatted(booksCount)).build();
        }
        return Health.down().withDetail("message", "(FAHRENHEIT 451) There are no books").build();
    }
}