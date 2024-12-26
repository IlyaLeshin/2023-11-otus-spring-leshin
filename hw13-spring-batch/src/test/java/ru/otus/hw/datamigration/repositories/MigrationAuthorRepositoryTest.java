package ru.otus.hw.datamigration.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@SpringBootTest
class MigrationAuthorRepositoryTest {

    @Autowired
    private MigrationAuthorRepository migrationAuthorRepository;

    @DisplayName("должен получать следующее значенние последовательности id авторов из БД")
    @ParameterizedTest
    @MethodSource("getId")
    void getNextSequenceIdTest(Long expectedId) {
        var actualId = migrationAuthorRepository.getNextSequenceId();
        assertThat(actualId)
                .isEqualTo(expectedId);
    }

    private static List<Long> getId() {
        return LongStream.range(1L, 10L).boxed()
                .toList();
    }
}