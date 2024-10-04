package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с жанрами должен")
@SpringBootTest(classes = GenreServiceImpl.class)
public class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private GenreConverter genreConverter;

    private List<GenreDto> dbGenreDtos;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenreDtos = getDbGenreDtos();
        dbGenres = getDbGenres();
    }

    @DisplayName("загружать список всех жанров. текущий метод: findAll()")
    @Test
    void findAllTest() {
        List<GenreDto> expectedGenreDtos = dbGenreDtos;
        for (int i = 0; i < dbGenres.size(); i++) {
            when(genreConverter.modelToDto(dbGenres.get(i))).thenReturn(dbGenreDtos.get(i));
        }
        when(genreRepository.findAll()).thenReturn(dbGenres);
        List<GenreDto> actualGenreDtos = genreService.findAll();

        assertThat(actualGenreDtos).containsExactlyElementsOf(expectedGenreDtos);
    }

    private static List<GenreDto> getDbGenreDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto("g" + id, "Genre_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre("g" + id, "Genre_" + id))
                .toList();
    }
}
