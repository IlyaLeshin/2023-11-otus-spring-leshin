package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.services.CommentService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер комментариев должен")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Configuration
    static class TestConfiguration {
        @Bean
        public CommentController bookController(CommentService commentService) {
            return new CommentController(commentService);
        }
    }

    private static final String FIRST_COMMENT_ID = "c1";

    private static final String FIRST_BOOK_ID = "b1";

    private static final CommentDto FIRST_COMMENT_DTO = new CommentDto(FIRST_COMMENT_ID, "Comment_1",
            FIRST_BOOK_ID);

    private static final CommentCreateDto COMMENT_CREATE_DTO = new CommentCreateDto("Comment_1",
            FIRST_BOOK_ID);


    private static final CommentCreateDto COMMENT_CREATE_DTO_WITH_INVALID_TEXT = new CommentCreateDto(null,
            FIRST_BOOK_ID);

    private static final CommentUpdateDto COMMENT_UPDATE_DTO = new CommentUpdateDto(FIRST_COMMENT_ID, "Comment_1",
            FIRST_BOOK_ID);

    private static final CommentUpdateDto COMMENT_UPDATE_DTO_WITH_INVALID_TEXT = new CommentUpdateDto(FIRST_COMMENT_ID, null,
            FIRST_BOOK_ID);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @DisplayName("получать комментарий")
    @Test
    void getCommentTest() throws Exception {
        when(commentService.findById(FIRST_COMMENT_ID)).thenReturn(FIRST_COMMENT_DTO);

        mvc.perform(get("/api/v1/books/{bookId}/comments/{commentId}", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(FIRST_COMMENT_DTO)));
    }

    @DisplayName("отправлять запрос на добавление книги")
    @Test
    void createComment() throws Exception {
        mvc.perform(post("/api/v1/books/{bookId}/comments", FIRST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_CREATE_DTO)))
                .andExpect(status().isCreated());

        verify(commentService, times(1)).create(COMMENT_CREATE_DTO);
    }

    @DisplayName("не отправлять запрос на добавление книги, если поля не допустимые")
    @Test
    void notCreateCommentWithInvalidFieldsTest() throws Exception {
        mvc.perform(post("/api/v1/books/{bookId}/comments", FIRST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_CREATE_DTO_WITH_INVALID_TEXT)))
                .andExpect(status().isBadRequest());

        verify(commentService, times(0)).create(COMMENT_CREATE_DTO_WITH_INVALID_TEXT);
    }

    @DisplayName("отправлять запрос на изменение книги")
    @Test
    void editComment() throws Exception {
        mvc.perform(put("/api/v1/books/{bookId}/comments/{commentId}", FIRST_BOOK_ID, FIRST_COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_UPDATE_DTO)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).update(COMMENT_UPDATE_DTO);
    }

    @DisplayName("не отправлять запрос на изменение книги, если поля не допустимые")
    @Test
    void notEditCommentWithInvalidFieldsTest() throws Exception {
        mvc.perform(put("/api/v1/books/{bookId}/comments/{commentId}", FIRST_BOOK_ID, FIRST_COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_UPDATE_DTO_WITH_INVALID_TEXT)))
                .andExpect(status().isBadRequest());

        verify(commentService, times(0)).update(COMMENT_UPDATE_DTO_WITH_INVALID_TEXT);
    }

    @DisplayName("отправлять запрос на удаление книги")
    @Test
    void deleteComment() throws Exception {
        mvc.perform(delete("/api/v1/books/{bookId}/comments/{commentId}", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteById(FIRST_COMMENT_ID);
    }
}