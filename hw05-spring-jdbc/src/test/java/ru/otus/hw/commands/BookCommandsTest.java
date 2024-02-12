package ru.otus.hw.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
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

    private List<BookDto> dbBookDtos;

    @BeforeEach
    void setUp() {
        dbBookDtos = getDbBookDtos(getDbAuthorDtos(), getDbGenreDtos());
    }

    @DisplayName("выводить в консоль все найденные книги. текущий метод findAllBooks()")
    @Test
    void findAllBooksTest() {
        String expectedBook = "Id: 1, title: BookTitle_1," + System.lineSeparator() +
                "Id: 2, title: BookTitle_2," + System.lineSeparator() +
                "Id: 3, title: BookTitle_3";
        when(bookService.findAll()).thenReturn(dbBookDtos);
        for (int i = 0; i < dbBookDtos.size(); i++) {
            when(bookConverter.dtoToString(dbBookDtos.get(i)))
                    .thenReturn("Id: %s, title: BookTitle_%s".formatted(i + 1, i + 1));
        }
        String actualGenres = bookCommands.findAllBooks();

        assertThat(actualGenres).isEqualTo(expectedBook);
    }

    @DisplayName("выводить в консоль найденную книгу по id. текущий метод findBookById(long id)")
    @Test
    void findBookByIdTest() {
        String expectedBook = "Id: 2, title: BookTitle_2";
        when(bookService.findById(2L)).thenReturn(Optional.ofNullable(dbBookDtos.get(1)));
        when(bookConverter.dtoToString(dbBookDtos.get(1))).thenReturn("Id: 2, title: BookTitle_2");
        String actualBook = bookCommands.findBookById(2L);

        assertThat(actualBook).isEqualTo(expectedBook);

    }

    @DisplayName("выводить в консоль сохраненную книгу. текущий метод insertBook(String title, long authorId, Set<Long> genresIds)")
    @Test
    void insertBookTest() {
        String expectedBook = "Id: 3, title: BookTitle_3";
        Set<Long> genresIds = new HashSet<>();
        BookDto bookDto = new BookDto();
        when(bookService.insert("BookTitle_3", 3, genresIds)).thenReturn(bookDto);
        when(bookConverter.dtoToString(bookDto)).thenReturn("Id: 3, title: BookTitle_3");
        String actualBook = bookCommands.insertBook("BookTitle_3", 3, genresIds);

        verify(bookService).insert("BookTitle_3", 3, genresIds);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("выводить в консоль обновленную книгу. текущий метод updateBook(long id, String title, long authorId, Set<Long> genresIds)")
    @Test
    void updateBookTest() {
        String expectedBook = "Id: 3, title: BookTitle_3";
        Set<Long> genresIds = new HashSet<>();
        BookDto bookDto = new BookDto(3L, "BookTitle_1", new AuthorDto(1, "Author_1"), new ArrayList<>());
        when(bookService.update(3L, "BookTitle_3", 3, genresIds)).thenReturn(bookDto);
        when(bookConverter.dtoToString(bookDto)).thenReturn("Id: 3, title: BookTitle_3");
        String actualBook = bookCommands.updateBook(3L, "BookTitle_3", 3, genresIds);

        verify(bookService).update(3L, "BookTitle_3", 3, genresIds);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("отправлять запрос на удаление книги. текущий метод deleteBook(long id)")
    @Test
    void deleteBook() {
        bookCommands.deleteBook(1);
        verify(bookService).deleteById(1);
    }

    private static List<AuthorDto> getDbAuthorDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenreDtos() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBookDtos(List<AuthorDto> dbAuthorDtos, List<GenreDto> dbGenreDtos) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto((long) id,
                        "BookTitle_" + id,
                        dbAuthorDtos.get(id - 1),
                        dbGenreDtos.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }
}
