package ru.otus.hw.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конвертер для работы с комментариями должен")
@SpringBootTest(classes = {CommentConverter.class})
public class CommentConverterTest {
    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String FIRST_BOOK_ID = "b1";

    private static final String FIRST_COMMENT_ID = "c1";

    @Autowired
    private CommentConverter commentConverter;

    private Comment comment;

    private CommentDto commentDto;


    @BeforeEach
    void setUp() {
        Author author = getAuthor();
        List<Genre> genres = getGenres();
        Book book = getBook(author, genres);
        comment = getComment(book);

        commentDto = getCommentDto();
    }

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод modelToDto(Comment comment)")
    @Test
    void modelToDtoTest() {
        CommentDto expectedCommentDto = commentDto;

        CommentDto actualCommentDto = commentConverter.modelToDto(comment);

        assertThat(actualCommentDto).isEqualTo(expectedCommentDto);
    }

    private static Author getAuthor() {
        return new Author(FIRST_AUTHOR_ID, "Author_1");
    }

    private static List<Genre> getGenres() {
        return IntStream.range(1, 3).boxed().map(id -> new Genre("g" + id, "Genre_" + id)).toList();
    }

    private static Book getBook(Author author, List<Genre> dbGenres) {
        return new Book(FIRST_BOOK_ID, "BookTitle_1", author, dbGenres);
    }

    private static Comment getComment(Book book) {
        return new Comment(FIRST_COMMENT_ID, "Comment_1", book);
    }

    private static CommentDto getCommentDto() {
        return new CommentDto(FIRST_COMMENT_ID, "Comment_1", FIRST_BOOK_ID);
    }
}
