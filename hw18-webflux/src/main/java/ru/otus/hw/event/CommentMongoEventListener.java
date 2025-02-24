package ru.otus.hw.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;


import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMongoEventListener extends AbstractMongoEventListener<Comment> {

    private final BookRepository bookRepository;


    @Override
    public void onAfterSave(AfterSaveEvent<Comment> event) {
        super.onAfterSave(event);
        Comment comment = event.getSource();
        Book book = comment.getBook();
        List<Comment> commentList = new ArrayList<>();
        if (book.getComments() != null) {
            commentList =
                    new ArrayList<>(book.getComments().stream()
                            .filter(c -> !c.getId().equals(comment.getId()))
                            .toList());

        }
        commentList.add(comment);
        book.setComments(commentList);

        bookRepository.save(book).subscribe();
    }

}
