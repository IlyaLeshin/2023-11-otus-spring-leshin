package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.datamigration.cache.MigrationAuthorCache;
import ru.otus.hw.datamigration.cache.MigrationBookCache;
import ru.otus.hw.datamigration.cache.MigrationBookIdCache;
import ru.otus.hw.datamigration.cache.MigrationGenreCache;
import ru.otus.hw.datamigration.models.MigrationAuthor;
import ru.otus.hw.datamigration.models.MigrationBook;
import ru.otus.hw.datamigration.models.MigrationGenre;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BookProcessor.class)
@DisplayName("Процессор для преобразования с книг должен")
class BookProcessorTest {

    private final String AUTHOR_MONGO_ID = "Author_mongo_id";

    private final Long AUTHOR_SQL_ID = 1L;

    private final String GENRE_MONGO_ID = "Genre_mongo_id";

    private final Long GENRE_SQL_ID = 1L;

    private final String BOOK_MONGO_ID = "Book_mongo_id";

    private final Long BOOK_SQL_ID = 1L;

    @Autowired
    BookProcessor bookProcessor;

    @MockBean
    MigrationAuthorCache migrationAuthorCache;

    @MockBean
    MigrationGenreCache migrationGenreCache;

    @MockBean
    MigrationBookCache migrationBookCache;

    @MockBean
    MigrationBookIdCache migrationBookIdCache;

    @DisplayName("преобразовывать Book в MigrationBook. текущий метод: process()")
    @Test
    void process() throws Exception {
        Author mongoAuthor = new Author(AUTHOR_MONGO_ID, "Author");
        Genre mongoGenre = new Genre(GENRE_MONGO_ID, "Genre");
        Book mongoBook = new Book(BOOK_MONGO_ID, "BOOK", mongoAuthor, List.of(mongoGenre));
        MigrationAuthor migrationAuthor = new MigrationAuthor(AUTHOR_SQL_ID, "Author");
        MigrationGenre migrationGenre = new MigrationGenre(GENRE_SQL_ID, "Genre");

        when(migrationAuthorCache.get(AUTHOR_MONGO_ID)).thenReturn(migrationAuthor);
        when(migrationGenreCache.get(GENRE_MONGO_ID)).thenReturn(migrationGenre);

        when(migrationBookIdCache.getNext()).thenReturn(1L);
        MigrationBook migrationBook = bookProcessor.process(mongoBook);
        assertThat(migrationBook).isNotNull();
        assertThat(migrationBook.getTitle()).isEqualTo(mongoBook.getTitle());
        assertThat(migrationBook.getAuthor()).isEqualTo(migrationAuthor);
        assertThat(migrationBook.getTitle()).isEqualTo(mongoBook.getTitle());
        assertThat(migrationBook.getId()).isEqualTo(BOOK_SQL_ID);

    }
}