package ru.otus.hw.datamigration.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("должен получать следующий диапазон значенний последовательности id авторов из БД")
    @Test
    void getNextSequenceRangeIdsTest()

    {
        var expectedIdList = getFirstRangeIds();
        var actualId = migrationAuthorRepository.getNextSequenceRangeIds(expectedIdList.size());

        assertThat(actualId)
                .isEqualTo(expectedIdList);

        var expectedSecondIdList = getSecondRangeIds();
        var secondActualId = migrationAuthorRepository.getNextSequenceRangeIds(expectedIdList.size());
        assertThat(secondActualId)
                .isEqualTo(expectedSecondIdList);
    }

    private static List<Long> getFirstRangeIds() {
        return LongStream.range(1L, 11L).boxed()
                .toList();
    }

    private static List<Long> getSecondRangeIds() {
        return LongStream.range(11L, 21L).boxed()
                .toList();
    }
}