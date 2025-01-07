package ru.otus.hw.datamigration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.datamigration.models.MigrationAuthor;

import java.util.List;

public interface MigrationAuthorRepository extends JpaRepository<MigrationAuthor, Long> {
    @Query(value = "select next value for author_seq from system_range(1, :range)", nativeQuery = true)
    List<Long> getNextSequenceRangeIds(@Param("range") long range);
}
