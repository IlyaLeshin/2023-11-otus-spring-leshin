package ru.otus.hw.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Конвертер для работы с авторами должен")
@SpringBootTest(classes = AuthorConverter.class)
public class AuthorConverterTest {

    private static final long FIRST_AUTHOR_ID = 1L;

    @Autowired
    private AuthorConverter authorConverter;

    private Author author;

    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        author = new Author(FIRST_AUTHOR_ID, "Author_1");
        authorDto = new AuthorDto(FIRST_AUTHOR_ID, "Author_1");
    }

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод modelToDto(Author author)")
    @Test
    void modelToDtoTest() {
        AuthorDto expectedAuthorDto = authorDto;
        AuthorDto actualAuthorDto = authorConverter.modelToDto(author);

        assertThat(actualAuthorDto).isEqualTo(expectedAuthorDto);
    }
}