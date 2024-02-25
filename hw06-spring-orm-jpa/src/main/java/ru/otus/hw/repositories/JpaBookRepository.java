package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("author-genres-entity-graph");
        Map<String, Object> params = Collections.singletonMap(FETCH.getKey(), entityGraph);
        return Optional.ofNullable(entityManager.find(Book.class, id, params));
    }

    @Override
    public Optional<Book> findWithCommentsById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("author-genres-comments-entity-graph");
        Map<String, Object> params = Collections.singletonMap(FETCH.getKey(), entityGraph);
        return Optional.ofNullable(entityManager.find(Book.class, id, params));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("author-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
