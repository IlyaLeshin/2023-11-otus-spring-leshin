package ru.otus.hw.controllers.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер страниц для комментариев должен")
@WebMvcTest(controllers = CommentPageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CommentPageControllerTest {

    private static final long FIRST_COMMENT_ID = 1L;

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @DisplayName("создавать страницу с комментарием к книге")
    @Test
    void commentPageTest() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/{commentId}", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("commentId"))
                .andExpect(view().name("comments/comment"));
    }

    @DisplayName("создавать страницу добавления комментария")
    @Test
    void creationFormCommentPage() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/creation-form", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("marker"))
                .andExpect(view().name("comments/editing-form"));
    }

    @DisplayName("создавать страницу редактирования комментария")
    @Test
    void editingFormCommentPage() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/{commentId}/editing-form", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookId"))
                .andExpect(model().attributeExists("commentId"))
                .andExpect(view().name("comments/editing-form"));
    }
}