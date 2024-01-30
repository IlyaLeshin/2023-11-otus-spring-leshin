package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        List<Book> books = namedParameterJdbcOperations
                .query("""
                        select books.id, books.title, books.author_id,
                        authors.full_name, genres.id, genres.name from books
                        inner join authors on books.author_id = authors.id
                        inner join books_genres on books_genres.book_id = books.id
                        inner join genres on genres.id = books_genres.genre_id
                        where books.id = :id
                        """, Map.of("id", id), new BookResultSetExtractor());
        if (books != null) {
            return books.stream().findFirst();
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from books where id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcOperations
                .query("""
                        select books.id, books.title, books.author_id, authors.full_name from books
                        left join authors on books.author_id = authors.id""", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcOperations
                .query("select book_id, genre_id from books_genres", new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Book> bookMap = new HashMap<>();
        for (Book book : booksWithoutGenres) {
            book.setGenres(new ArrayList<>());
            bookMap.put(book.getId(), book);
        }
        Map<Long, Genre> genreMap = new HashMap<>();
        for (Genre genre : genres) {
            genreMap.put(genre.getId(), genre);
        }
        for (BookGenreRelation bookGenreRelation : relations) {
            var bookId = bookGenreRelation.bookId();
            var genreId = bookGenreRelation.genreId();

            bookMap.get(bookId).getGenres().add(genreMap.get(genreId));
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId());
        namedParameterJdbcOperations.update("insert into books (title, author_id) values (:title, :author_id)",
                mapSqlParameterSource, keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private Book update(Book book) {
        namedParameterJdbcOperations.update("update books set title = :title, author_id=:author_id where id =:id",
                Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor().getId()));
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(
                book.getGenres().stream().map(genre -> new BookGenreRelation(book.getId(), genre.getId())).toList());
        namedParameterJdbcOperations
                .batchUpdate("insert into books_genres(book_id, genre_id) values (:bookId, :genreId)", batchArgs);
    }

    private void removeGenresRelationsFor(Book book) {
        namedParameterJdbcOperations.update("delete from books_genres where book_id = :id", Map.of("id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("books.id");
            String title = rs.getString("books.title");
            long authorId = rs.getLong("books.author_id");
            String authorName = rs.getString("authors.full_name");

            return new Book(id, title, new Author(authorId, authorName), new ArrayList<>());
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> bookMap = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("books.id");
                Book book;
                if (bookMap.containsKey(id)) {
                    book = bookMap.get(id);
                } else {
                    String title = rs.getString("books.title");
                    long authorId = rs.getLong("books.author_id");
                    String authorFullName = rs.getString("authors.full_name");
                    book = new Book(id, title, new Author(authorId, authorFullName), new ArrayList<>());
                    bookMap.put(id, book);
                }
                long genreId = rs.getLong("genres.id");
                String genreName = rs.getString("genres.name");
                book.getGenres().add(new Genre(genreId, genreName));
            }
            return bookMap.values().stream().toList();
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");

            return new BookGenreRelation(bookId, genreId);
        }
    }
}
