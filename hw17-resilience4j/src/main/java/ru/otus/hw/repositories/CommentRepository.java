package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
