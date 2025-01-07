package ru.otus.hw.datamigration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.datamigration.models.MigrationBook;

import java.util.List;

public interface MigrationBookRepository extends JpaRepository<MigrationBook, Long> {
    @Query(value = "select next value for book_seq from system_range(1, :range)", nativeQuery = true)
    List<Long> getNextSequenceRangeIds(@Param("range") long range);
}
