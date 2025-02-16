package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Контроллер авторов должен")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Configuration
    static class TestConfiguration {
        @Bean
        public AuthorController AuthorController(AuthorService authorService) {
            return new AuthorController(authorService);
        }
    }

    private static final String FIRST_AUTHOR_ID = "a1";
    private static final String SECOND_AUTHOR_ID = "a2";

    private static final AuthorDto FIRST_AUTHOR_DTO = new AuthorDto(FIRST_AUTHOR_ID, "AUTHOR_1");

    private static final AuthorDto SECOND_AUTHOR_DTO = new AuthorDto(SECOND_AUTHOR_ID, "AUTHOR_2");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @DisplayName("Получить список всех авторов")
    @Test
    void getListAuthorsTest() throws Exception {
        var authorsList = List.of(FIRST_AUTHOR_DTO, SECOND_AUTHOR_DTO);
        when(authorService.findAll()).thenReturn(authorsList);

        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authorsList)));
    }
}