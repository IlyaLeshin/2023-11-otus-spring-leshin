package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.datamigration.dto.BookMigrationDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(classes = BookProcessor.class)
@DisplayName("Процессор для преобразования с книг должен")
class BookProcessorTest {

    private final String AUTHOR_MONGO_ID = "Author_mongo_id";

    private final String GENRE_MONGO_ID = "Genre_mongo_id";

    private final String BOOK_MONGO_ID = "Book_mongo_id";

    @Autowired
    BookProcessor bookProcessor;

    @DisplayName("преобразовывать Book в MigrationBook. текущий метод: process()")
    @Test
    void process() throws Exception {
        Author mongoAuthor = new Author(AUTHOR_MONGO_ID, "Author");
        Genre mongoGenre = new Genre(GENRE_MONGO_ID, "Genre");
        Book mongoBook = new Book(BOOK_MONGO_ID, "BOOK", mongoAuthor, List.of(mongoGenre));


        BookMigrationDto migrationBook = bookProcessor.process(mongoBook);
        assertThat(migrationBook).isNotNull();
        assertThat(migrationBook.getTitle()).isEqualTo(mongoBook.getTitle());
        assertThat(migrationBook.getAuthorId()).isEqualTo(AUTHOR_MONGO_ID);
        assertThat(migrationBook.getTitle()).isEqualTo(mongoBook.getTitle());
        assertThat(migrationBook.getId()).isEqualTo(BOOK_MONGO_ID);

    }
}