package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
class JpaGenreRepositoryTest {

    private static final int EXPECTED_GENRES_COUNT = 6;

    private static final long FIRST_GENRE_ID = 1L;

    private static final long THIRD_GENRE_ID = 3L;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать список всех жанров по id")
    @Test
    void shouldReturnCorrectGenresListById() {
        var genres = genreRepository.findAllByIdIn(Set.of(FIRST_GENRE_ID, THIRD_GENRE_ID));

        assertNotNull(genres);
        assertEquals(2, genres.size());
        assertEquals("Genre_1", genres.get(0).getName());
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var genres = genreRepository.findAll();

        assertThat(genres).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .allMatch(genre -> !genre.getName().isEmpty())
                .containsOnlyOnce(testEntityManager.find(Genre.class, FIRST_GENRE_ID))
                .containsOnlyOnce(testEntityManager.find(Genre.class, THIRD_GENRE_ID));
    }
}