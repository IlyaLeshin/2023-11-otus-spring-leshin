package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ChangeLog
public class DatabaseChangelog {

    private List<Author> authorList = List.of();

    private List<Genre> genreList = List.of();

    private List<Book> bookList = List.of();

    private List<Comment> commentList = List.of();

    @ChangeSet(order = "001", id = "dropDb", author = "ilyaLeshin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "ilyaLeshin")
    public void insertAuthors(AuthorRepository authorRepository) {
        authorList = authorRepository.saveAll(createAuthors());
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "ilyaLeshin")
    public void insertGenres(GenreRepository genreRepository) {
        genreList = genreRepository.saveAll(createGenres());
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "ilyaLeshin")
    public void insertBooks(BookRepository bookRepository) {
        bookList = bookRepository.saveAll(createBooks(authorList, genreList));
    }

    @ChangeSet(order = "005", id = "insertCommentsForBooks", author = "ilyaLeshin")
    public void insertCommentsForBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        for (Book book : bookList) {
            List<Comment> comments = createCommentsForBook(book);
            commentRepository.saveAll(comments);
            book.setComments(comments);
            bookRepository.save(book);
        }

    }

    private List<Author> createAuthors() {
        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            authors.add(new Author(null, "Author_" + i));
        }
        return authors;
    }

    private List<Genre> createGenres() {
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            genres.add(new Genre(null, "Genre" + i));
        }
        return genres;
    }

    private List<Book> createBooks(List<Author> authorList, List<Genre> genreList) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Book book = new Book();
            book.setTitle("Book_" + i);
            int authorIdx = random(authorList.size());
            book.setAuthor(authorList.get(authorIdx));
            Set<Genre> genres = new HashSet<>();
            for (int g = 0; g < random(5); g++) {
                genres.add(genreList.get(random(genreList.size())));
            }
            book.setGenres(genres.stream().toList());
            books.add(book);
        }

        return books;
    }

    private List<Comment> createCommentsForBook(Book book) {
        List<Comment> comments = new ArrayList<>();
        for (int c = 0; c < random(10); c++) {
            Comment comment = new Comment(null, "Comment_" + c, book);
            comments.add(comment);
        }
        return comments;
    }

    private int random(int max) {
        return (int) (Math.random() * max);
    }

}
