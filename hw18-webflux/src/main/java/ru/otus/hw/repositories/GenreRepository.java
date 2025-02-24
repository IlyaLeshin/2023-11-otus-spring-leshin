package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.Genre;

import reactor.core.publisher.Flux;
import java.util.Set;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findAllByIdIn(Set<String> genresIds);
}
