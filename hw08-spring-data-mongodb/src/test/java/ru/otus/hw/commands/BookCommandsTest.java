package ru.otus.hw.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.BookWithCommentsConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Команды для работы с книгами должны")
@SpringBootTest(classes = BookCommands.class)
public class BookCommandsTest {
    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String THIRD_AUTHOR_ID = "a3";

    private static final String FIRST_BOOK_ID = "b1";

    private static final String SECOND_BOOK_ID = "b2";

    private static final String THIRD_BOOK_ID = "b3";

    @Autowired
    private BookCommands bookCommands;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookConverter bookConverter;

    @MockBean
    private BookWithCommentsConverter bookWithCommentsConverter;

    private List<BookDto> dbBookDtos;

    @BeforeEach
    void setUp() {
        dbBookDtos = getDbBookDtos(getDbAuthorDtos(), getDbGenreDtos());
    }

    @DisplayName("выводить в консоль все найденные книги. текущий метод findAllBooks()")
    @Test
    void findAllBooksTest() {
        String separator = System.lineSeparator();
        String expectedBook = "Id: %s, title: BookTitle_1,".formatted(FIRST_BOOK_ID) + separator +
                "Id: %s, title: BookTitle_2,".formatted(SECOND_BOOK_ID) + separator +
                "Id: %s, title: BookTitle_3".formatted(THIRD_BOOK_ID);
        when(bookService.findAll()).thenReturn(dbBookDtos);
        for (int i = 0; i < dbBookDtos.size(); i++) {
            when(bookConverter.dtoToString(dbBookDtos.get(i)))
                    .thenReturn("Id: b%s, title: BookTitle_%s".formatted(i + 1, i + 1));
        }
        String actualGenres = bookCommands.findAllBooks();

        assertThat(actualGenres).isEqualTo(expectedBook);
    }

    @DisplayName("выводить в консоль найденную книгу по id. текущий метод findBookById(long id)")
    @Test
    void findBookByIdTest() {
        String expectedBook = "Id: %s, title: BookTitle_2".formatted(SECOND_BOOK_ID);
        when(bookService.findById(SECOND_BOOK_ID)).thenReturn(Optional.ofNullable(dbBookDtos.get(1)));
        when(bookConverter.dtoToString(dbBookDtos.get(1))).thenReturn(expectedBook);
        String actualBook = bookCommands.findBookById(SECOND_BOOK_ID);

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("выводить в консоль найденную книгу по id. текущий метод findBookById(long id)")
    @Test
    void findBookWithCommentsByIdTest() {
        var dbBookWithCommentsDtos = getDbBookWithCommentsDtos(getDbAuthorDtos(), getDbGenreDtos());
        String expectedBook = "Id: %s, title: BookTitle_2".formatted(SECOND_BOOK_ID);
        when(bookService.findWithCommentsById(SECOND_BOOK_ID)).thenReturn(Optional.ofNullable(dbBookWithCommentsDtos.get(1)));
        when(bookWithCommentsConverter.dtoToString(dbBookWithCommentsDtos.get(1))).thenReturn(expectedBook);
        String actualBook = bookCommands.findBookWithCommentsById(SECOND_BOOK_ID);

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("выводить в консоль сохраненную книгу. текущий метод insertBook(String title, long authorId, Set<Long> genresIds)")
    @Test
    void insertBookTest() {
        String expectedBook = "Id: %s, title: BookTitle_3".formatted(THIRD_BOOK_ID);
        Set<String> genresIds = new HashSet<>();
        BookDto bookDto = new BookDto();
        when(bookService.insert("BookTitle_3", THIRD_AUTHOR_ID, genresIds)).thenReturn(bookDto);
        when(bookConverter.dtoToString(bookDto)).thenReturn(expectedBook);
        String actualBook = bookCommands.insertBook("BookTitle_3", THIRD_AUTHOR_ID, genresIds);

        verify(bookService).insert("BookTitle_3", THIRD_AUTHOR_ID, genresIds);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("выводить в консоль обновленную книгу. текущий метод updateBook(long id, String title, long authorId, Set<Long> genresIds)")
    @Test
    void updateBookTest() {
        String expectedBook = "Id: %s, title: UpdatedBookTitle_3".formatted(THIRD_BOOK_ID);
        Set<String> genresIds = new HashSet<>();
        BookDto bookDto = new BookDto(THIRD_BOOK_ID, "UpdatedBookTitle_3",
                new AuthorDto(FIRST_AUTHOR_ID, "Author_1"),
                List.of());
        when(bookService.update(THIRD_BOOK_ID, "UpdatedBookTitle_3", FIRST_AUTHOR_ID, genresIds)).thenReturn(bookDto);
        when(bookConverter.dtoToString(bookDto)).thenReturn(expectedBook);
        String actualBook = bookCommands.updateBook(THIRD_BOOK_ID, "UpdatedBookTitle_3", FIRST_AUTHOR_ID, genresIds);

        verify(bookService).update(THIRD_BOOK_ID, "UpdatedBookTitle_3", FIRST_AUTHOR_ID, genresIds);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("отправлять запрос на удаление книги. текущий метод deleteBook(long id)")
    @Test
    void deleteBook() {
        bookCommands.deleteBook(FIRST_BOOK_ID);
        verify(bookService).deleteById(FIRST_BOOK_ID);
    }

    private static List<AuthorDto> getDbAuthorDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto("a" + id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenreDtos() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto("g" + id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBookDtos(List<AuthorDto> dbAuthorDtos, List<GenreDto> dbGenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto("b" + id,
                        "BookTitle_" + id,
                        dbAuthorDtos.get(id - 1),
                        dbGenreDtos.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<BookWithCommentsDto> getDbBookWithCommentsDtos(List<AuthorDto> dbAuthorDtos, List<GenreDto> dbGenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookWithCommentsDto("b" + id,
                        "BookTitle_" + id,
                        dbAuthorDtos.get(id - 1),
                        dbGenreDtos.subList((id - 1) * 2, (id - 1) * 2 + 2), List.of()
                ))
                .toList();
    }
}
