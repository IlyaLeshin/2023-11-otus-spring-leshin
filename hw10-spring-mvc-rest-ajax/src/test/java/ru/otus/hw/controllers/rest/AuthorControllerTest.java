package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Контроллер авторов должен")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final long SECOND_AUTHOR_ID = 2L;

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