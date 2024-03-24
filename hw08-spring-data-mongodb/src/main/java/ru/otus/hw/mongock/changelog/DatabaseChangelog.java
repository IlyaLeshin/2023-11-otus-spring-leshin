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

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private List<Author> authorList;

    private List<Genre> genreList;

    private List<Book> bookList;

    private List<Comment> commentList;

    @ChangeSet(order = "001", id = "dropDb", author = "IlyaLeshin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "IlyaLeshin")
    public void insertAuthors(AuthorRepository authorRepository) {
        var authors = List.of(new Author(null, "Author_1"),
                new Author(null, "Author_2"), new Author(null, "Author_3"));

        authorList = authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "IlyaLeshin")
    public void insertGenres(GenreRepository genreRepository) {
        var genres = List.of(new Genre(null, "Genre_1"), new Genre(null, "Genre_2"),
                new Genre(null, "Genre_3"), new Genre(null, "Genre_4"),
                new Genre(null, "Genre_5"), new Genre(null, "Genre_6"));

        genreList = genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "IlyaLeshin")
    public void insertBooks(BookRepository bookRepository) {
        var authorOne = authorList.get(0);
        var authorTwo = authorList.get(1);
        var authorThree = authorList.get(1);

        var genreOne = genreList.get(0);
        var genreTwo = genreList.get(1);
        var genreFour = genreList.get(3);
        var genreFive = genreList.get(4);

        var books = List.of(new Book(null, "BookTitle_1", authorOne, List.of(genreOne, genreTwo)),
                new Book(null, "BookTitle_2", authorTwo, List.of(genreTwo, genreFour)),
                new Book(null, "BookTitle_3", authorThree, List.of(genreTwo, genreFive)));

        bookList = bookRepository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertCommentsForBooks", author = "IlyaLeshin")
    public void insertCommentsForBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        var bookOne = bookList.get(0);
        var bookTwo = bookList.get(1);

        var comments = List.of(new Comment(null, "Comment_1", bookOne),
                new Comment(null, "Comment_2", bookOne),
                new Comment(null, "Comment_3", bookOne),
                new Comment(null, "Comment_4", bookTwo),
                new Comment(null, "Comment_5", bookTwo),
                new Comment(null, "Comment_6", bookTwo));

        commentList = commentRepository.saveAll(comments);
    }

    @ChangeSet(order = "006", id = "referenceBookComments", author = "IlyaLeshin", runAlways = true)
    public void referenceBookComments(BookRepository bookRepository) {
        bookList.get(0).setComments(List.of(commentList.get(0), commentList.get(1)));
        bookList.get(1).setComments(List.of(commentList.get(3), commentList.get(4), commentList.get(5)));

        bookRepository.save(bookList.get(0));
        bookRepository.save(bookList.get(1));
    }
}
