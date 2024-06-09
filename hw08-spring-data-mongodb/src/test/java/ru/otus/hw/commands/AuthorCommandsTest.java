package ru.otus.hw.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Команды для работы с авторами должны")
@SpringBootTest(classes = AuthorCommands.class)
public class AuthorCommandsTest {
    private static final String FIRST_AUTHOR_ID = "a1";

    private static final String SECOND_AUTHOR_ID = "a2";

    private static final String THIRD_AUTHOR_ID = "a3";

    @Autowired
    private AuthorCommands authorCommands;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorConverter authorConverter;

    private List<AuthorDto> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("выводить в консоль всех авторов. текущий метод findAllAuthors()")
    @Test
    void findAllAuthorsTest() {
        String separator = System.lineSeparator();
        String expectedAuthors = "Id: %s, FullName: Author_1,".formatted(FIRST_AUTHOR_ID) + separator +
                "Id: %s, FullName: Author_2,".formatted(SECOND_AUTHOR_ID) + separator +
                "Id: %s, FullName: Author_3".formatted(THIRD_AUTHOR_ID);
        when(authorService.findAll()).thenReturn(dbAuthors);
        for (int i = 0; i < dbAuthors.size(); i++) {
            when(authorConverter.dtoToString(dbAuthors.get(i)))
                    .thenReturn("Id: a%s, FullName: Author_%s".formatted(i + 1, i + 1));
        }
        String actualAuthors = authorCommands.findAllAuthors();

        assertThat(actualAuthors)
                .isEqualTo(expectedAuthors);
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto("a" + id, "Author_" + id))
                .toList();
    }
}
