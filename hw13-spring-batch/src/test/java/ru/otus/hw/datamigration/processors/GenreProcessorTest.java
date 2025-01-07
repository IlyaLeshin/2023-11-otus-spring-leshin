package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.datamigration.cache.MigrationGenreCache;
import ru.otus.hw.datamigration.cache.MigrationGenreIdCache;
import ru.otus.hw.datamigration.models.MigrationGenre;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Процессор для преобразования с жанров должен")
@SpringBootTest(classes = GenreProcessor.class)
class GenreProcessorTest {

    @Autowired
    GenreProcessor genreProcessor;

    @MockBean
    MigrationGenreCache migrationGenreCache;

    @MockBean
    MigrationGenreIdCache migrationGenreIdCache;

    @DisplayName("преобразовывать Genre в MigrationGenre. текущий метод: process()")
    @Test
    void processTest() throws Exception {
        Genre genre = new Genre("ObjectId_1", "Genre_1");

        when(migrationGenreIdCache.getNext()).thenReturn(1L);
        MigrationGenre migrationGenre = genreProcessor.process(genre);

        assertThat(migrationGenre).isNotNull();
        assertThat(migrationGenre.getName()).isEqualTo(genre.getName());
        assertThat(migrationGenre.getId()).isEqualTo(1L);
    }
}