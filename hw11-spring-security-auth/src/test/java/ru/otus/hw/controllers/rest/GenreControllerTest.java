package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер жанров должен")
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    private static final long FIRST_GENRE_ID = 1L;
    private static final long SECOND_GENRE_ID = 2L;

    private static final GenreDto FIRST_GENRE_DTO = new GenreDto(FIRST_GENRE_ID, "GENRE_1");

    private static final GenreDto SECOND_GENRE_DTO = new GenreDto(SECOND_GENRE_ID, "GENRE_2");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @DisplayName("Получить список всех жанров")
    @Test
    void getListAuthorsTest() throws Exception {
        var genresList = List.of(FIRST_GENRE_DTO, SECOND_GENRE_DTO);
        when(genreService.findAll()).thenReturn(genresList);

        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(genresList)));
    }
}