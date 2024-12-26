package ru.otus.hw.datamigration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.datamigration.models.MigrationBook;

public interface MigrationBookRepository extends JpaRepository<MigrationBook, Long> {
    @Query(value = "select next value for book_seq", nativeQuery = true)
    Long getNextSequenceId();
}
