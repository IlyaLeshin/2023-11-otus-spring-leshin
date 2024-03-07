package ru.otus.hw.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Команды для работы с комментариями должны")
@SpringBootTest(classes = CommentCommands.class)
class CommentCommandsTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final long FIRST_COMMENT_ID = 1L;

    @Autowired
    private CommentCommands commentCommands;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentConverter commentConverter;

    List<Comment> comments;

    List<CommentDto> commentDtos;

    @BeforeEach
    void setUp() {
        Author author = getAuthor();
        List<Genre> genres = getGenres();
        Book book = getBook(author, genres);
        comments = getComments(book);

        commentDtos = getCommentDtos();
    }


    @DisplayName("выводить в консоль все комментарии для книги по id книги. текущий метод findAllCommentsByBookId(long bookId)")
    @Test
    void findAllCommentsByBookIdTest() {
        String expectedComment = "Id: 1, Text: Comment_1," + System.lineSeparator() + "Id: 2, Text: Comment_2";
        when(commentService.findAllByBookId(FIRST_BOOK_ID)).thenReturn(commentDtos);
        for (int i = 0; i < commentDtos.size(); i++) {
            when(commentConverter.dtoToString(commentDtos.get(i)))
                    .thenReturn("Id: %s, Text: Comment_%s".formatted(i + 1, i + 1));
        }
        String actualComment = commentCommands.findAllCommentsByBookId(FIRST_BOOK_ID);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("выводить в консоль комментарий для книги по id. текущий метод findCommentById(long id)")
    @Test
    void findCommentByIdTest() {
        String expectedComment = "Id: 1, Text: Comment_1";
        when(commentService.findById(FIRST_COMMENT_ID)).thenReturn(Optional.ofNullable(commentDtos.get(0)));
        when(commentConverter.dtoToString(commentDtos.get(0))).thenReturn("Id: 1, Text: Comment_1");

        String actualComment = commentCommands.findCommentById(FIRST_COMMENT_ID);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("Создавать комментарий для книги. текущий метод createComment(String content, long bookId)")
    @Test
    void createCommentTest() {
        String expectedComment = "Id: 3, Text: Comment_3";
        CommentDto commentDto = new CommentDto();
        when(commentService.create("Comment_3", 1)).thenReturn(commentDto);
        when(commentConverter.dtoToString(commentDto)).thenReturn("Id: 3, Text: Comment_3");
        String actualComment = commentCommands.createComment("Comment_3", 1);

        verify(commentService).create("Comment_3", 1);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("Обновлять комментарий для книги. текущий метод updateComment(long id, String content, long bookId)")
    @Test
    void updateCommentTest() {
        String expectedComment = "Id: 1, Text: Comment_3";
        CommentDto commentDto = new CommentDto();
        when(commentService.update(FIRST_COMMENT_ID, "Comment_3", FIRST_BOOK_ID)).thenReturn(commentDto);
        when(commentConverter.dtoToString(commentDto)).thenReturn("Id: 1, Text: Comment_3");
        String actualComment = commentCommands.updateComment(FIRST_COMMENT_ID, "Comment_3", FIRST_BOOK_ID);

        verify(commentService).update(FIRST_COMMENT_ID, "Comment_3", FIRST_BOOK_ID);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("Удалять комментарий для книги по id. текущий метод deleteComment(long id)")
    @Test
    void deleteCommentTest() {
        commentCommands.deleteComment(FIRST_COMMENT_ID);
        verify(commentService).deleteById(FIRST_COMMENT_ID);
    }

    private static Author getAuthor() {
        return new Author(1, "Author_1");
    }

    private static List<Genre> getGenres() {
        return IntStream.range(1, 3).boxed().map(id -> new Genre(id, "Genre_" + id)).toList();
    }

    private static Book getBook(Author author, List<Genre> dbGenres) {
        return new Book(FIRST_BOOK_ID, "BookTitle_1", author, dbGenres);
    }

    private static List<Comment> getComments(Book book) {
        return IntStream.range(1, 3).boxed().map(id -> new Comment(id, "Comment_" + id, book)).toList();
    }

    private static List<CommentDto> getCommentDtos() {
        return IntStream.range(1, 3).boxed().map(id -> new CommentDto(id, "Comment_" + id, FIRST_BOOK_ID)).toList();
    }
}