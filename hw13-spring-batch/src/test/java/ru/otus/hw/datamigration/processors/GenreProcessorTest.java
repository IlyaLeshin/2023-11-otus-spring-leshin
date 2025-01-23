package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.datamigration.dto.GenreMigrationDto;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Процессор для преобразования с жанров должен")
@SpringBootTest(classes = GenreProcessor.class)
class GenreProcessorTest {

    @Autowired
    GenreProcessor genreProcessor;


    @DisplayName("преобразовывать Genre в MigrationGenre. текущий метод: process()")
    @Test
    void processTest() throws Exception {
        Genre genre = new Genre("ObjectId_1", "Genre_1");

        GenreMigrationDto migrationGenre = genreProcessor.process(genre);

        assertThat(migrationGenre).isNotNull();
        assertThat(migrationGenre.getName()).isEqualTo(genre.getName());
        assertThat(migrationGenre.getId()).isEqualTo("ObjectId_1");
    }
}