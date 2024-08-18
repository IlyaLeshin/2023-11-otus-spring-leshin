package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с комментариями должен")
@SpringBootTest(classes = CommentServiceImpl.class)
class CommentServiceImplTest {
    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String FIRST_BOOK_ID = "b1";

    private static final String FIRST_COMMENT_ID = "c1";


    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentConverter commentConverter;

    private Book book;

    List<Comment> comments;

    List<CommentDto> commentDtos;

    @BeforeEach
    void setUp() {
        Author author = getAuthor();
        List<Genre> genres = getGenres();
        book = getBook(author, genres);
        comments = getComments(book);
        commentDtos = getCommentDtos();
        book.setComments(comments);
    }

    @DisplayName("загружать комментарий по id. текущий метод: findById(long id)")
    @Test
    void findByIdTest() {
        CommentDto expectedComment = commentDtos.get(0);
        when(commentRepository.findById(FIRST_COMMENT_ID)).thenReturn(Optional.ofNullable(comments.get(0)));
        when(commentConverter.modelToDto(comments.get(0))).thenReturn(commentDtos.get(0));

        CommentDto actualComment = commentService.findById(FIRST_COMMENT_ID);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("загружать все комментарии по id книги. текущий метод: findAllByBookId(long id)")
    @Test
    void findAllByBookIdTest() {
        List<CommentDto> expectedCommentList = commentDtos;
        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.ofNullable(book));
        when(commentRepository.findAllByBookId(FIRST_BOOK_ID)).thenReturn(comments);
        for (int i = 0; i < comments.size(); i++) {
            when(commentConverter.modelToDto(comments.get(i)))
                    .thenReturn(commentDtos.get(i));
        }
        List<CommentDto> actualCommentsList = commentService.findAllByBookId(FIRST_BOOK_ID);

        assertThat(actualCommentsList).isEqualTo(expectedCommentList);

    }

    @DisplayName("создавать комментарий для книги. текущий метод: create(String text, long bookId)")
    @Test
    void create() {
        var newComment = new Comment(null, "saved_Comment", book);
        var newCommentDto = new CommentDto(null, "saved_Comment", newComment.getBook().getId());
        var expectedCommentDto = new CommentDto(FIRST_COMMENT_ID, "saved_Comment", newComment.getBook().getId());
        when(bookRepository.findById(FIRST_BOOK_ID)).thenReturn(Optional.ofNullable(book));
        when(commentConverter.modelToDto(commentRepository.save(newComment))).thenReturn(expectedCommentDto);

        CommentDto returnedCommentDto = commentService.create(newCommentDto);

        verify(commentRepository).save(newComment);
        verify(bookRepository).save(book);
        assertThat(returnedCommentDto).isEqualTo(expectedCommentDto);
    }

    @DisplayName("изменять комментарий для книги. текущий метод: update(long id, String text, long bookId)")
    @Test
    void updateTest() {
        var commentBeforeUpdate = comments.get(0);
        var commentToUpdate = new Comment(FIRST_COMMENT_ID, "updated_Comment", book);
        var expectedCommentDto = new CommentDto(FIRST_COMMENT_ID, "updated_Comment", book.getId());
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(commentRepository.findById(FIRST_COMMENT_ID)).thenReturn(Optional.ofNullable(commentBeforeUpdate));
        when(commentConverter.modelToDto(Optional.ofNullable(commentBeforeUpdate).get()))
                .thenReturn(commentDtos.get(0));
        when(commentConverter.modelToDto(commentRepository.save(commentToUpdate))).thenReturn(expectedCommentDto);

        CommentDto actualComment = commentService.update(expectedCommentDto);

        verify(commentRepository).save(commentToUpdate);
        verify(bookRepository).save(book);
        assertThat(actualComment).isEqualTo(expectedCommentDto);
    }

    @DisplayName("Удалять комментарий по id. текущий метод: deleteById(long id)")
    @Test
    void deleteById() {
        commentService.deleteById(FIRST_COMMENT_ID);
        verify(commentRepository).deleteById(FIRST_COMMENT_ID);
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

    private static List<CommentDto> getCommentDtos() {
        return IntStream.range(1, 3).boxed().map(id -> new CommentDto("c" + id, "Comment_" + id, FIRST_BOOK_ID)).toList();
    }
}