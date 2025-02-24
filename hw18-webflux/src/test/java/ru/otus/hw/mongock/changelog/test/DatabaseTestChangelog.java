package ru.otus.hw.mongock.changelog.test;

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
public class DatabaseTestChangelog {

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
        authorList = List.of(new Author("a1", "Author_1"),
                new Author("a2", "Author_2"), new Author("a3", "Author_3"));

        authorRepository.saveAll(authorList).collectList().block();
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "ilyaLeshin")
    public void insertGenres(GenreRepository genreRepository) {
        genreList = List.of(new Genre("g1", "Genre_1"), new Genre("g2", "Genre_2"),
                new Genre("g3", "Genre_3"), new Genre("g4", "Genre_4"),
                new Genre("g5", "Genre_5"), new Genre("g6", "Genre_6"));

        genreRepository.saveAll(genreList).collectList().block();
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "ilyaLeshin")
    public void insertBooks(BookRepository bookRepository) {
        var authorOne = authorList.get(0);
        var authorTwo = authorList.get(1);
        var authorThree = authorList.get(1);

        var genreOne = genreList.get(0);
        var genreTwo = genreList.get(1);
        var genreFour = genreList.get(3);
        var genreFive = genreList.get(4);

        bookList = List.of(new Book("b1", "BookTitle_1", authorOne, List.of(genreOne, genreTwo)),
                new Book("b2", "BookTitle_2", authorTwo, List.of(genreTwo, genreFour)),
                new Book("b3", "BookTitle_3", authorThree, List.of(genreTwo, genreFive)));

        bookRepository.saveAll(bookList).collectList().block();
    }

    @ChangeSet(order = "005", id = "insertCommentsForBooks", author = "ilyaLeshin")
    public void insertCommentsForBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        var bookOne = bookList.get(0);
        var bookTwo = bookList.get(1);

        commentList = List.of(new Comment("c1", "Comment_1", bookOne),
                new Comment("c2", "Comment_2", bookOne),
                new Comment("c3", "Comment_3", bookOne),
                new Comment("c4", "Comment_4", bookTwo),
                new Comment("c5", "Comment_5", bookTwo),
                new Comment("c6", "Comment_6", bookTwo));

        commentRepository.saveAll(commentList).collectList().block();

        bookList.get(0).setComments(List.of(commentList.get(0), commentList.get(1), commentList.get(2)));
        bookList.get(1).setComments(List.of(commentList.get(3), commentList.get(4), commentList.get(5)));
        bookRepository.save(bookList.get(0)).block();
        bookRepository.save(bookList.get(1)).block();
    }

}
