package ru.otus.hw.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.*;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Конвертер для работы с книгами и списком комментариев должен")
@SpringBootTest(classes = {BookWithCommentsConverter.class})
public class BookWithCommentsConverterTest {
    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String FIRST_GENRE_ID = "g1";

    private static final String SECOND_GENRE_ID = "g2";

    private static final String FIRST_BOOK_ID = "b1";

    private static final String FIRST_COMMENT_ID = "c1";

    private static final String SECOND_COMMENT_ID = "c2";

    @Autowired
    private BookWithCommentsConverter bookWithCommentsConverter;

    @MockBean
    private AuthorConverter authorConverter;

    @MockBean
    private GenreConverter genreConverter;

    @MockBean
    private CommentConverter commentConverter;

    private Book bookWithComments;

    private List<GenreDto> genreDtos;

    private List<CommentDto> commentDtos;

    private BookWithCommentsDto bookWithCommentsDto;

    @BeforeEach
    void setUp() {
        Author author = getAuthor();
        List<Genre> genres = getGenres();
        Book book = getBook(author, genres);
        List<Comment> comments = getComments(book);
        bookWithComments = getBookWithComments(author, genres, comments);
        AuthorDto authorDto = getAuthorDto();
        genreDtos = getGenreDtos();
        commentDtos = getCommentDtos();
        bookWithCommentsDto = getBookWithCommentsDto(authorDto, genreDtos, commentDtos);
    }

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод modelToDtoTest(Book book)")
    @Test
    void modelToDtoTest() {
        BookWithCommentsDto expectedBookDto = bookWithCommentsDto;
        when(authorConverter.modelToDto(bookWithComments.getAuthor())).thenReturn(bookWithCommentsDto.getAuthorDto());
        for (int i = 0; i < genreDtos.size(); i++) {
            when(genreConverter.modelToDto(bookWithComments.getGenres().get(i)))
                    .thenReturn(bookWithCommentsDto.getGenreDtoList().get(i));
        }
        for (int i = 0; i < commentDtos.size(); i++) {
            when(commentConverter.modelToDto(bookWithComments.getComments().get(i)))
                    .thenReturn(bookWithCommentsDto.getCommentDtoList().get(i));
        }
        BookWithCommentsDto actualBookDto = bookWithCommentsConverter.modelToDto(bookWithComments);

        assertThat(actualBookDto).isEqualTo(expectedBookDto);
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

    private static List<Comment> getComments(Book book) {
        return IntStream.range(1, 3).boxed().map(id -> new Comment("c" + id, "Comment_" + id, book)).toList();
    }

    private static Book getBookWithComments(Author author, List<Genre> dbGenres, List<Comment> dbComments) {
        return new Book(FIRST_BOOK_ID, "BookTitle_1", author, dbGenres, dbComments);
    }

    private static AuthorDto getAuthorDto() {
        return new AuthorDto(FIRST_AUTHOR_ID, "Author_1");
    }

    private static List<GenreDto> getGenreDtos() {
        return IntStream.range(1, 3).boxed().map(id -> new GenreDto("g" + id, "Genre_" + id)).toList();
    }

    private static List<CommentDto> getCommentDtos() {
        return IntStream.range(1, 3).boxed().map(id -> new CommentDto("c" + id, "Comment_" + id,
                FIRST_BOOK_ID)).toList();
    }

    private static BookWithCommentsDto getBookWithCommentsDto(AuthorDto author,
                                                              List<GenreDto> dbGenreDtos,
                                                              List<CommentDto> dbCommentsDtos) {
        return new BookWithCommentsDto(FIRST_BOOK_ID, "BookTitle_1", author, dbGenreDtos, dbCommentsDtos);
    }
}
