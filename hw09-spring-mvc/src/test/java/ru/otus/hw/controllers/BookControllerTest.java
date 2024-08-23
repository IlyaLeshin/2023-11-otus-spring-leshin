package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.BookWithCommentsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен")
@WebMvcTest(BookController.class)
@AutoConfigureDataMongo
class BookControllerTest {
    private static final String FIRST_BOOK_ID = "b1";
    private static final String SECOND_BOOK_ID = "b2";
    private static final String FIRST_AUTHOR_ID = "a1";
    private static final String SECOND_AUTHOR_ID = "a1";
    private static final String FIRST_GENRE_ID = "g1";
    private static final String SECOND_GENRE_ID = "g2";

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

    private static final BookUpdateDto FIRST_BOOK_UPDATE_DTO = new BookUpdateDto(FIRST_BOOK.getId(),
            FIRST_BOOK.getTitle(), FIRST_AUTHOR_ID, Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID));

    private static final BookWithCommentsDto FIRST_BOOK_WITH_COMMENTS_DTO = new BookWithCommentsDto(FIRST_BOOK_ID,
            "Book_1", FIRST_AUTHOR_DTO, List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO), List.of());

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookConverter bookConverter;

    @Test
    void booksPageTest() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(FIRST_BOOK_DTO, SECOND_BOOK_DTO));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(content().string(containsString(FIRST_BOOK_UPDATE_DTO.getTitle())))
                .andExpect(content().string(containsString(SECOND_BOOK_DTO.getTitle())));
    }

    @Test
    void bookWithCommentsPage() throws Exception {
        when(bookService.findWithCommentsById(FIRST_BOOK_ID)).thenReturn(FIRST_BOOK_WITH_COMMENTS_DTO);

        mvc.perform(get("/books/b1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(content().string(containsString(FIRST_BOOK_WITH_COMMENTS_DTO.getTitle())));
    }

    @Test
    void createPage() throws Exception {
        when(authorService.findAll()).thenReturn(List.of(FIRST_AUTHOR_DTO, SECOND_AUTHOR_DTO));
        when(genreService.findAll()).thenReturn(List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO));

        mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("authors",
                        List.of(FIRST_AUTHOR_DTO, SECOND_AUTHOR_DTO)))
                .andExpect(model().attribute("genres",
                        List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO)));
    }


    @Test
    void createBook() throws Exception {
        mvc.perform(post("/books/create").flashAttr("book", BOOK_CREATE_DTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).insert(BOOK_CREATE_DTO);
    }

    @Test
    void editPage() throws Exception {
        when(bookService.findById(FIRST_BOOK_ID)).thenReturn(FIRST_BOOK_DTO);
        when(bookConverter.dtoToUpdateDto(FIRST_BOOK_DTO)).thenReturn(FIRST_BOOK_UPDATE_DTO);
        when(authorService.findAll()).thenReturn(List.of(FIRST_AUTHOR_DTO, SECOND_AUTHOR_DTO));
        when(genreService.findAll()).thenReturn(List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO));

        mvc.perform(get("/books/b1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FIRST_BOOK_UPDATE_DTO.getTitle())))
                .andExpect(content().string(containsString(FIRST_AUTHOR.getFullName())))
                .andExpect(content().string(containsString(SECOND_AUTHOR.getFullName())))
                .andExpect(content().string(containsString(FIRST_GENRE.getName())))
                .andExpect(content().string(containsString(SECOND_GENRE.getName())));
    }

    @Test
    void editBook() throws Exception {
        mvc.perform(post("/books/{id}/edit", FIRST_BOOK_ID).flashAttr("book", FIRST_BOOK_UPDATE_DTO))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(1)).update(FIRST_BOOK_UPDATE_DTO);
    }

    @Test
    void delete() throws Exception {
        mvc.perform(post("/books/{id}/delete", FIRST_BOOK_ID))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(1)).deleteById(FIRST_BOOK_ID);
    }
}
