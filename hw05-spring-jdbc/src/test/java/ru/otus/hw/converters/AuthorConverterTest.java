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
    @Autowired
    private AuthorConverter authorConverter;

    private Author author;

    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        author = new Author(1, "Author_1");
        authorDto = new AuthorDto(1, "Author_1");
    }

    @DisplayName("корректно преобразовывать DTO в строку. текущий метод authorToString(AuthorDto author)")
    @Test
    void authorToStringTest() {
        String expectedAuthor = "Id: 1, FullName: Author_1";
        String actualAuthor = authorConverter.dtoToString(authorDto);

        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    @DisplayName("корректно преобразовывать DTO в модель. текущий метод authorDtoToAuthor(AuthorDto authorDto)")
    @Test
    void authorDtoToAuthorTest() {
        Author expectedAuthor = author;
        Author actualAuthor = authorConverter.dtoToModel(authorDto);

        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    @DisplayName("корректно преобразовывать модель в DTO. текущий метод authorToAuthorDto(Author author)")
    @Test
    void authorToAuthorDtoTest() {
        AuthorDto expectedAuthorDto = authorDto;
        AuthorDto actualAuthorDto = authorConverter.modelToDto(author);

        assertThat(actualAuthorDto).isEqualTo(expectedAuthorDto);
    }
}