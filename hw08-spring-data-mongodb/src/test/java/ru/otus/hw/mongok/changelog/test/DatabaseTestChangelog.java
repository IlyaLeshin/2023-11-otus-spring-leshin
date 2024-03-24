package ru.otus.hw.mongok.changelog.test;

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

    private List<Author> authorList;

    private List<Genre> genreList;

    private List<Book> bookList;

    private List<Comment> commentList;

    @ChangeSet(order = "001", id = "dropDb", author = "ilyaLeshin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "ilyaLeshin")
    public void insertAuthors(AuthorRepository authorRepository) {
        for (int i = 0; i < 3; i++) {
            authorList.add(authorRepository.save(new Author("a" + i, "Author_" + i + 1)));
        }
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "ilyaLeshin")
    public void insertGenres(GenreRepository genreRepository) {
        for (int i = 0; i < 6; i++) {
            genreList.add(genreRepository.save(new Genre("g" + i + 1, "Genre_" + i + 1)));
        }
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

        var books = List.of(new Book("b1", "BookTitle_1", authorOne, List.of(genreOne, genreTwo)),
                new Book("b2", "BookTitle_2", authorTwo, List.of(genreTwo, genreFour)),
                new Book("b3", "BookTitle_3", authorThree, List.of(genreTwo, genreFive)));

        bookList = bookRepository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertCommentsForBooks", author = "ilyaLeshin")
    public void insertCommentsForBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        var bookOne = bookList.get(0);
        var bookTwo = bookList.get(1);

        var comments = List.of(new Comment("c1", "Comment_1", bookOne),
                new Comment("c2", "Comment_2", bookOne),
                new Comment("c3", "Comment_3", bookOne),
                new Comment("c4", "Comment_4", bookTwo),
                new Comment("c5", "Comment_5", bookTwo),
                new Comment("c6", "Comment_6", bookTwo));

        commentList = commentRepository.saveAll(comments);
    }

    @ChangeSet(order = "006", id = "referenceBookComments", author = "ilyaLeshin", runAlways = true)
    public void referenceBookComments(BookRepository repository) {

        bookList.get(0).setComments(List.of(commentList.get(0), commentList.get(1)));
        bookList.get(1).setComments(List.of(commentList.get(3), commentList.get(4), commentList.get(5)));

        repository.save(bookList.get(0));
        repository.save(bookList.get(1));
    }
}
