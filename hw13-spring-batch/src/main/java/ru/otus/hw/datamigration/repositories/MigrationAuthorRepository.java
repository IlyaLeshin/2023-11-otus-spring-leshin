package ru.otus.hw.datamigration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.datamigration.models.MigrationAuthor;

public interface MigrationAuthorRepository extends JpaRepository<MigrationAuthor, Long> {
    @Query(value = "select next value for author_seq", nativeQuery = true)
    Long getNextSequenceId();
}
