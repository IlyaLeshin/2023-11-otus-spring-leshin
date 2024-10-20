package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Контроллер книг должен")
@WebMvcTest(controllers = BookController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BookControllerTest {
    private static final long FIRST_BOOK_ID = 1L;
    private static final long SECOND_BOOK_ID = 2L;
    private static final long FIRST_AUTHOR_ID = 1L;
    private static final long SECOND_AUTHOR_ID = 2L;
    private static final long FIRST_GENRE_ID = 1L;
    private static final long SECOND_GENRE_ID = 2L;

    private static final Author FIRST_AUTHOR = new Author(FIRST_AUTHOR_ID, "AUTHOR_1");

    private static final Author SECOND_AUTHOR = new Author(SECOND_AUTHOR_ID, "AUTHOR_2");

    private static final AuthorDto FIRST_AUTHOR_DTO = new AuthorDto(FIRST_AUTHOR.getId(),
            FIRST_AUTHOR.getFullName());

    private static final AuthorDto SECOND_AUTHOR_DTO = new AuthorDto(SECOND_AUTHOR.getId(),
            SECOND_AUTHOR.getFullName());

    private static final Genre FIRST_GENRE = new Genre(FIRST_GENRE_ID, "GENRE_1");

    private static final GenreDto FIRST_GENRE_DTO = new GenreDto(FIRST_GENRE.getId(),
            FIRST_GENRE.getName());

    private static final Genre SECOND_GENRE = new Genre(SECOND_GENRE_ID, "GENRE_2");

    private static final GenreDto SECOND_GENRE_DTO = new GenreDto(SECOND_GENRE.getId(),
            SECOND_GENRE.getName());

    private static final Book FIRST_BOOK = new Book(FIRST_BOOK_ID, "BOOK_1", FIRST_AUTHOR,
            List.of(FIRST_GENRE, SECOND_GENRE), null);

    private static final Book SECOND_BOOK = new Book(SECOND_BOOK_ID, "BOOK_2", SECOND_AUTHOR,
            List.of(FIRST_GENRE, SECOND_GENRE), null);

    private static final BookDto FIRST_BOOK_DTO = new BookDto(FIRST_BOOK.getId(),
            FIRST_BOOK.getTitle(),
            FIRST_AUTHOR_DTO, List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO));

    private static final BookDto SECOND_BOOK_DTO = new BookDto(SECOND_BOOK.getId(),
            SECOND_BOOK.getTitle(),
            SECOND_AUTHOR_DTO, List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO));

    private static final BookCreateDto BOOK_CREATE_DTO = new BookCreateDto(FIRST_BOOK.getTitle(), FIRST_AUTHOR_ID,
            Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookCreateDto BOOK_CREATE_DTO_WITH_INVALID_TEXT = new BookCreateDto(null, FIRST_AUTHOR_ID,
            Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookCreateDto BOOK_CREATE_DTO_WITH_INVALID_AUTHOR = new BookCreateDto(FIRST_BOOK.getTitle(),
            null, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookCreateDto BOOK_CREATE_DTO_WITH_INVALID_GENRES = new BookCreateDto(FIRST_BOOK.getTitle(),
            FIRST_AUTHOR_ID, null);

    private static final BookUpdateDto FIRST_BOOK_UPDATE_DTO = new BookUpdateDto(FIRST_BOOK.getId(),
            FIRST_BOOK.getTitle(), FIRST_AUTHOR_ID, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookUpdateDto BOOK_UPDATE_DTO_WITH_INVALID_TEXT = new BookUpdateDto(FIRST_BOOK.getId(),
            null, FIRST_AUTHOR_ID, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookUpdateDto BOOK_UPDATE_DTO_WITH_INVALID_AUTHOR = new BookUpdateDto(FIRST_BOOK.getId(),
            FIRST_BOOK.getTitle(), null, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookUpdateDto BOOK_UPDATE_DTO_WITH_INVALID_GENRES = new BookUpdateDto(FIRST_BOOK.getId(),
            FIRST_BOOK.getTitle(), FIRST_AUTHOR_ID, null);

    private static final BookWithCommentsDto FIRST_BOOK_WITH_COMMENTS_DTO = new BookWithCommentsDto(FIRST_BOOK_ID,
            "Book_1", FIRST_AUTHOR_DTO, List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO), List.of());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @DisplayName("получать список всех книг")
    @Test
    void getListBooksTest() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(FIRST_BOOK_DTO, SECOND_BOOK_DTO));

        mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk());
    }

    @DisplayName("получать книгу с комментариями")
    @Test
    void getBookWithCommentsTest() throws Exception {
        when(bookService.findWithCommentsById(FIRST_BOOK_ID)).thenReturn(FIRST_BOOK_WITH_COMMENTS_DTO);

        mvc.perform(get("/api/v1/books/{id}", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FIRST_BOOK_WITH_COMMENTS_DTO)));
    }

    @DisplayName("отправлять запрос на добавление книги")
    @Test
    void createBookTest() throws Exception {
        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOK_CREATE_DTO)))
                .andExpect(status().isCreated());

        verify(bookService, times(1)).insert(BOOK_CREATE_DTO);
    }

    @DisplayName("не отправлять запрос на добавление книги, если поля не допустимые")
    @ParameterizedTest
    @MethodSource("invalidCreateDtos")
    void notCreateBookWithInvalidFieldsTest(BookCreateDto bookCreateDto) throws Exception {
        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDto)))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).insert(bookCreateDto);
    }

    @DisplayName("отправлять запрос на изменение книги")
    @Test
    void editBookTest() throws Exception {
        mvc.perform(put("/api/v1/books/{id}", FIRST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FIRST_BOOK_UPDATE_DTO)))
                .andExpect(status().isOk());

        verify(bookService, times(1)).update(FIRST_BOOK_UPDATE_DTO);
    }

    @DisplayName("не отправлять запрос на изменение книги, если поля не допустимые")
    @ParameterizedTest
    @MethodSource("invalidUpdateDtos")
    void notEditBookWithInvalidFieldsTest(BookUpdateDto bookUpdateDto) throws Exception {
        mvc.perform(put("/api/v1/books/{id}", FIRST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookUpdateDto)))
                .andExpect(status().isBadRequest());

        verify(bookService, times(0)).update(bookUpdateDto);
    }

    @DisplayName("отправлять запрос на удаление книги")
    @Test
    void deleteBookTest() throws Exception {
        mvc.perform(delete("/api/v1/books/{id}", FIRST_BOOK_ID))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(FIRST_BOOK_ID);
    }

    private static List<BookCreateDto> invalidCreateDtos() {
        return List.of(BOOK_CREATE_DTO_WITH_INVALID_TEXT,
                BOOK_CREATE_DTO_WITH_INVALID_AUTHOR, BOOK_CREATE_DTO_WITH_INVALID_GENRES);
    }

    private static List<BookUpdateDto> invalidUpdateDtos() {
        return List.of(BOOK_UPDATE_DTO_WITH_INVALID_TEXT,
                BOOK_UPDATE_DTO_WITH_INVALID_AUTHOR, BOOK_UPDATE_DTO_WITH_INVALID_GENRES);
    }
}