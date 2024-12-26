package ru.otus.hw.datamigration.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@SpringBootTest
class MigrationBookRepositoryTest {

    @Autowired
    private MigrationBookRepository migrationBookRepository;

    @DisplayName("должен получать следующее значенние последовательности id книг из БД")
    @ParameterizedTest
    @MethodSource("getId")
    void getNextSequenceIdTest(Long expectedId) {
        var actualId = migrationBookRepository.getNextSequenceId();
        assertThat(actualId)
                .isEqualTo(expectedId);
    }

    private static List<Long> getId() {
        return LongStream.range(1L, 10L).boxed()
                .toList();
    }
}