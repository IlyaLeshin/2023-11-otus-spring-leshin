package ru.otus.hw.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конвертер для работы с книгами должен")
@SpringBootTest(classes = {BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class BookConverterTest {
    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private AuthorConverter authorConverter;

    @Autowired
    private GenreConverter genreConverter;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        Author author = getAuthor();
        List<Genre> genres = getGenres();
        book = getBook(author, genres);
        AuthorDto authorDto = getAuthorDto();
        List<GenreDto> genreDtos = getGenreDtos();
        bookDto = getBookDto(authorDto, genreDtos);
    }

    @DisplayName("корректно преобразовывать DTO в строку. текущий метод bookToString(BookDto book)")
    @Test
    void bookToStringTest() {
        String expectedBook="Id: 1, title: BookTitle_1," +
                " author: {Id: 1, FullName: Author_1}," +
                " genres: [{Id: 1, Name: Genre_1}, {Id: 2, Name: Genre_2}]";
        String actualBook = bookConverter.dtoToString(bookDto);

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("корректно преобразовывать DTO в модель. текущий метод bookDtoToBook(BookDto bookDto)")
    @Test
    void bookDtoToBook() {
        Book expectedBook = book;
        Book actualBook = bookConverter.dtoToModel(bookDto);

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод bookToBookDto(Book book)")
    @Test
    void bookToBookDtoTest() {
        BookDto expectedBookDto = bookDto;
        BookDto actualBookDto = bookConverter.modelToDto(book);

        assertThat(actualBookDto).isEqualTo(expectedBookDto);
    }

    private static Author getAuthor() {
        return new Author(1, "Author_1");
    }

    private static List<Genre> getGenres() {
        return IntStream.range(1, 3).boxed().map(id -> new Genre(id, "Genre_" + id)).toList();
    }

    private static Book getBook(Author author, List<Genre> dbGenres) {
        return new Book(1, "BookTitle_1", author, dbGenres);
    }

    private static AuthorDto getAuthorDto() {
        return new AuthorDto(1, "Author_1");
    }

    private static List<GenreDto> getGenreDtos() {
        return IntStream.range(1, 3).boxed().map(id -> new GenreDto(id, "Genre_" + id)).toList();
    }

    private static BookDto getBookDto(AuthorDto author, List<GenreDto> dbGenreDtos) {
        return new BookDto(1L, "BookTitle_1", author, dbGenreDtos);
    }
}
