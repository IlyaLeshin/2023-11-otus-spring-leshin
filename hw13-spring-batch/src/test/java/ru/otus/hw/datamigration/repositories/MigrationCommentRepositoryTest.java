package ru.otus.hw.datamigration.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@SpringBootTest
class MigrationCommentRepositoryTest {

    @Autowired
    private MigrationCommentRepository migrationCommentRepository;

    @DisplayName("должен получать диапазон значенний последовательности id комментариев из БД")
    @Test
    void getNextSequenceRangeIdsTest() {
        var expectedIdList = getFirstRangeIds();
        var actualId = migrationCommentRepository.getNextSequenceRangeIds(expectedIdList.size());

        assertThat(actualId)
                .isEqualTo(expectedIdList);

        var expectedSecondIdList = getSecondRangeIds();
        var secondActualId = migrationCommentRepository.getNextSequenceRangeIds(expectedIdList.size());
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