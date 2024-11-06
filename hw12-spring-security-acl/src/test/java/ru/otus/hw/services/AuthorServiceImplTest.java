package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DisplayName("Сервис для работы с авторами должен")
@SpringBootTest(classes = {AuthorServiceImpl.class})

public class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private AuthorConverter authorConverter;

    private List<AuthorDto> dbAuthorDtos;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthorDtos = getAuthorDtos();
        dbAuthors = getAuthors();
    }

    @DisplayName("загружать список всех авторов. текущий метод: findAll()")
    @Test
    void findAllTest() {
        List<AuthorDto> expectedAuthorDtos = dbAuthorDtos;
        for (int i = 0; i < dbAuthors.size(); i++) {
            when(authorConverter.modelToDto(dbAuthors.get(i))).thenReturn(dbAuthorDtos.get(i));
        }
        when(authorRepository.findAll()).thenReturn(dbAuthors);
        List<AuthorDto> actualAuthorDtos = authorService.findAll();

        assertThat(actualAuthorDtos).containsExactlyElementsOf(expectedAuthorDtos);
    }

    private static List<AuthorDto> getAuthorDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<Author> getAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
