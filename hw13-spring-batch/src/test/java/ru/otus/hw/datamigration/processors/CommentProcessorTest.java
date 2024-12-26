package ru.otus.hw.datamigration.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.datamigration.cache.MigrationBookCache;
import ru.otus.hw.datamigration.dto.MigrationCommentDto;
import ru.otus.hw.datamigration.repositories.MigrationCommentRepository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CommentProcessor.class)
@DisplayName("Процессор для преобразования с комментариев книг должен")
class CommentProcessorTest {

    private final String AUTHOR_MONGO_ID = "Author_mongo_id";

    private final String BOOK_MONGO_ID = "Book_mongo_id";

    private final Long BOOK_SQL_ID = 1L;

    private final String COMMENT_MONGO_ID = "Author_mongo_id";

    private final Author AUTHOR = new Author(AUTHOR_MONGO_ID, "Author");

    private final Book BOOK = new Book(BOOK_MONGO_ID, "Book", AUTHOR, List.of());

    @Autowired
    CommentProcessor commentProcessor;

    @MockBean
    MigrationBookCache migrationBookCache;

    @MockBean
    MigrationCommentRepository migrationCommentRepository;

    @DisplayName("преобразовывать Book в MigrationBook. текущий метод: process()")
    @Test
    void process() throws Exception {
        Comment mongoComment = new Comment(COMMENT_MONGO_ID, "Comment", BOOK);

        when(migrationBookCache.get(BOOK_MONGO_ID)).thenReturn(BOOK_SQL_ID);

        when(migrationCommentRepository.getNextSequenceId()).thenReturn(BOOK_SQL_ID);
        MigrationCommentDto migrationCommentDto = commentProcessor.process(mongoComment);
        assertThat(migrationCommentDto).isNotNull();
        assertThat(migrationCommentDto.getText()).isEqualTo(mongoComment.getText());
        assertThat(migrationCommentDto.getBookId()).isEqualTo(BOOK_SQL_ID);
    }
}