package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с комментариями ")
@DataMongoTest
class CommentRepositoryTest {

    private static final String FIRST_BOOK_ID = "b1";

    private static final String FIRST_COMMENT_ID = "c1";

    private static final String SECOND_COMMENT_ID = "c2";

    private static final String FORTH_COMMENT_ID = "c4";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoOperations mongoOperations;


    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var optionalActualComment = commentRepository.findById(FIRST_COMMENT_ID);
        var expectedComment = mongoOperations.findById(FIRST_COMMENT_ID, Comment.class);

        assertThat(optionalActualComment)
                .isPresent().get().usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = mongoOperations.findById(FIRST_BOOK_ID, Book.class);
        var expectedComment = new Comment(null, "saved_Comment", book);
        var returnedComment = commentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty());

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent().get().usingRecursiveComparison().isEqualTo(returnedComment);
    }


    @DisplayName("должен сохранять(обновлять) комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = mongoOperations.findById(FIRST_BOOK_ID, Book.class);
        var newComment = new Comment(FORTH_COMMENT_ID, "updated_Comment", book);

        var expectedComment = commentRepository.save(newComment);

        assertThat(expectedComment).isNotNull();
        assertThat(expectedComment.getId()).isNotNull();

        var actualComment = mongoOperations.findById(expectedComment.getId(), Comment.class);

        assertThat(actualComment)
                .isNotNull().usingRecursiveComparison()
                .isEqualTo(expectedComment);

        assertThat(actualComment)
                .matches(comment -> Objects.equals(comment.getId(), FORTH_COMMENT_ID))
                .matches(comment -> comment.getText().equals("updated_Comment"))
                .matches(comment -> comment.getBook() != null && Objects.equals(comment.getBook().getId(), FIRST_BOOK_ID));
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBook() {
        var comment = commentRepository.findById(SECOND_COMMENT_ID);

        assertThat(comment).isPresent();

        commentRepository.deleteById(comment.get().getId());

        assertThat(commentRepository.findById(SECOND_COMMENT_ID)).isEmpty();
    }
}
