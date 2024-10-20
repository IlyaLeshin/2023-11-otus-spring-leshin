package ru.otus.hw.controllers.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер страниц для книг должен")
@WebMvcTest(BookPageController.class)
class BookPageControllerTest {
    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @DisplayName("создавать страницу со списком книг")
    @Test
    void booksPageTest() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list-of-books"));
    }

    @DisplayName("создавать страницу с книгой с комментариями")
    @Test
    void bookWithCommentsPageTest() throws Exception {
        mvc.perform(get("/books/{id}", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookId"))
                .andExpect(view().name("books/book-with-comments"));
                }

    @DisplayName("создавать страницу добавления книги")
    @Test
    void creationFormBookPageTest() throws Exception {
        mvc.perform(get("/books/creation-form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("marker"))
                .andExpect(view().name("books/editing-form"));
    }

    @DisplayName("создавать страницу редактирования книги")
    @Test
    void editingFormBookPageTest() throws Exception {
        mvc.perform(get("/books/{id}/editing-form", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookId"))
                .andExpect(view().name("books/editing-form"));
    }
}