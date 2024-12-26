package ru.otus.hw.datamigration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.datamigration.models.MigrationComment;

public interface MigrationCommentRepository extends JpaRepository<MigrationComment, Long> {
    @Query(value = "select next value for comment_seq", nativeQuery = true)
    Long getNextSequenceId();
}
