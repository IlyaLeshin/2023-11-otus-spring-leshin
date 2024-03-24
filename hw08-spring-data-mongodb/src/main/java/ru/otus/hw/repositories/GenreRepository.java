package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByName(String name);

    List<Genre> findAllByIdIn(Set<String> genresIds);
}
