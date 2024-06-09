package ru.otus.hw.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конвертер для работы с жанрами должен")
@SpringBootTest(classes = {GenreConverter.class})
public class GenreConverterTest {
    private static final String FIRST_GENRE_ID = "g1";

    @Autowired
    private GenreConverter genreConverter;

    private Genre genre;

    private GenreDto genreDto;

    @BeforeEach
    void setUp() {
        genre = getGenre();
        genreDto = getGenreDto();
    }

    @DisplayName("корректно преобразовывать DTO в строку. текущий метод modelToString(GenreDto genre)")
    @Test
    void modelToStringTest() {
        String expectedGenre = "Id: %s, Name: Genre_1".formatted(FIRST_GENRE_ID);
        String actualGenre = genreConverter.dtoToString(genreDto);

        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод modelToDto(Genre genre)")
    @Test
    void modelToDtoTest() {
        GenreDto expectedGenreDto = genreDto;
        GenreDto actualGenreDto = genreConverter.modelToDto(genre);

        assertThat(actualGenreDto).isEqualTo(expectedGenreDto);
    }

    private static Genre getGenre() {
        return new Genre(FIRST_GENRE_ID, "Genre_1");
    }

    private static GenreDto getGenreDto() {
        return new GenreDto(FIRST_GENRE_ID, "Genre_1");
    }
}
