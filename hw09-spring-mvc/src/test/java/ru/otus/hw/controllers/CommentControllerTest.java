package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Контроллер комментариев должен")
@WebMvcTest(CommentController.class)
@AutoConfigureDataMongo
class CommentControllerTest {
    private static final String FIRST_COMMENT_ID = "c1";

    private static final String FIRST_BOOK_ID = "b1";

    private static final CommentDto FIRST_COMMENT_DTO = new CommentDto(FIRST_COMMENT_ID, "Comment_1",
            FIRST_BOOK_ID);

    private static final CommentCreateDto COMMENT_CREATE_DTO = new CommentCreateDto("Comment_1",
            FIRST_BOOK_ID);

    private static final CommentUpdateDto COMMENT_UPDATE_DTO = new CommentUpdateDto(FIRST_COMMENT_ID, "Comment_1",
            FIRST_BOOK_ID);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentConverter commentConverter;

    @DisplayName("создавать страницу с комментарием")
    @Test
    void commentPageTest() throws Exception {
        when(commentService.findById(FIRST_COMMENT_ID)).thenReturn(FIRST_COMMENT_DTO);

        mvc.perform(get("/books/{bookId}/comments/{commentId}", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(content().string(containsString(FIRST_COMMENT_DTO.getText())));
    }

    @DisplayName("создавать страницу добавления комментария")
    @Test
    void createPageTest() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/create", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"));

    }

    @DisplayName("отправлять запрос на добавление комментария и возвращаться на страницу с книгой")
    @Test
    void createCommentTest() throws Exception {
        mvc.perform(post("/books/{bookId}/comments/create", FIRST_BOOK_ID)
                        .flashAttr("comment", COMMENT_CREATE_DTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/books/{bookId}", FIRST_BOOK_ID));
        verify(commentService, times(1)).create(COMMENT_CREATE_DTO);
    }

    @DisplayName("создавать страницу редактирования комментария")
    @Test
    void editPageTest() throws Exception {
        when(commentService.findById(FIRST_COMMENT_ID)).thenReturn(FIRST_COMMENT_DTO);
        when(commentConverter.dtoToUpdateDto(FIRST_COMMENT_DTO)).thenReturn(COMMENT_UPDATE_DTO);

        mvc.perform(get("/books/{bookId}/comments/{commentId}/edit", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(content().string(containsString(COMMENT_UPDATE_DTO.getText())));
    }

    @DisplayName("отправлять запрос на редактирование комментария и возвращаться на страницу с книгой")
    @Test
    void editBookTest() throws Exception {
        mvc.perform(post("/books/{bookId}/comments/{commentId}/edit", FIRST_BOOK_ID, FIRST_COMMENT_ID)
                        .flashAttr("comment", COMMENT_UPDATE_DTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/books/{bookId}", FIRST_BOOK_ID));
        verify(commentService, times(1)).update(COMMENT_UPDATE_DTO);
    }

    @DisplayName("отправлять запрос на удаление комментария и возвращаться на страницу с книгой")
    @Test
    void deleteTest() throws Exception {
        mvc.perform(post("/books/{bookId}/comments/{commentID}/delete", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/books/{bookId}", FIRST_BOOK_ID));
        verify(commentService, times(1)).deleteById(FIRST_COMMENT_ID);
    }
}