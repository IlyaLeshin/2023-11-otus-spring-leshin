package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final long FIRST_AUTHOR_ID = 1L;

    private static final long FIRST_GENRE_ID = 1L;

    private static final long FIRST_COMMENT_ID = 1L;

    private static final long FORTH_COMMENT_ID = 4L;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var optionalActualComment = jpaCommentRepository.findById(FIRST_COMMENT_ID);
        var expectedComment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);

        assertThat(optionalActualComment).isPresent().get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var author = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        var genre = testEntityManager.find(Genre.class, FIRST_GENRE_ID);
        var book = new Book(FIRST_BOOK_ID, "Book", author, List.of(genre));
        var expectedComment = new Comment(0, "saved_Comment", book);
        var returnedComment = jpaCommentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0);

        assertThat(jpaCommentRepository.findById(returnedComment.getId()))
                .isPresent().get().isEqualTo(returnedComment);
    }


    @DisplayName("должен сохранять комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var author = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        var genre = testEntityManager.find(Genre.class, FIRST_GENRE_ID);
        var book = new Book(FIRST_BOOK_ID, "Book", author, List.of(genre));

        var newComment = new Comment(FORTH_COMMENT_ID,"updated_Comment",book);

        var expectedComment = jpaCommentRepository.save(newComment);

        assertThat(expectedComment).isNotNull();
        assertThat(expectedComment.getId()).isGreaterThan(0);

        var actualComment = testEntityManager.find(Comment.class, expectedComment.getId());

        assertThat(actualComment)
                .isNotNull()
                .isEqualTo(expectedComment);

        assertThat(actualComment)
                .matches(comment -> comment.getId() == FORTH_COMMENT_ID)
                .matches(comment -> comment.getText().equals("updated_Comment"))
                .matches(comment -> comment.getBook() != null && comment.getBook().getId() == FIRST_BOOK_ID);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBook() {
        var comment = jpaCommentRepository.findById(FIRST_COMMENT_ID);

        assertThat(comment).isPresent();

        jpaCommentRepository.deleteById(comment.get().getId());

        assertThat(jpaCommentRepository.findById(FIRST_COMMENT_ID)).isEmpty();
    }
}
