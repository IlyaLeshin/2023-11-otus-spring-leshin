package ru.otus.hw.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Команды для работы с жанрами должны")
@SpringBootTest(classes = GenreCommands.class)
public class GenreCommandsTest {
    private static final String FIRST_GENRE_ID = "g1";

    private static final String SECOND_GENRE_ID = "g2";

    private static final String THIRD_GENRE_ID = "g3";

    @Autowired
    private GenreCommands genreCommands;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreConverter genreConverter;

    private List<GenreDto> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("выводить в консоль все жанры. текущий метод findAllGenres()")
    @Test
    void findAllGenresTest() {
        String separator = System.lineSeparator();
        String expectedGenres = "Id: %s, Name: Genre_1,".formatted(FIRST_GENRE_ID) + separator +
                "Id: %s, Name: Genre_2,".formatted(SECOND_GENRE_ID) + separator +
                "Id: %s, Name: Genre_3".formatted(THIRD_GENRE_ID);
        when(genreService.findAll()).thenReturn(dbGenres);
        for (int i = 0; i < dbGenres.size(); i++) {
            when(genreConverter.dtoToString(dbGenres.get(i)))
                    .thenReturn("Id: g%s, Name: Genre_%s".formatted(i + 1, i + 1));
        }

        String actualGenres = genreCommands.findAllGenres();

        assertThat(actualGenres)
                .isEqualTo(expectedGenres);
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto("g" + id, "Genre_" + id))
                .toList();
    }
}
