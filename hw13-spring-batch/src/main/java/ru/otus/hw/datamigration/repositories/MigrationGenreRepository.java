package ru.otus.hw.datamigration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.datamigration.models.MigrationGenre;

public interface MigrationGenreRepository extends JpaRepository<MigrationGenre, Long> {
    @Query(value = "select next value for genre_seq", nativeQuery = true)
    Long getNextSequenceId();
}
