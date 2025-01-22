package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.dto.CommentMigrationDto;
import ru.otus.hw.models.Comment;

@Component
@AllArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, CommentMigrationDto> {

    @Override
    public CommentMigrationDto process(@NonNull Comment item) throws Exception {
        try {

            return new CommentMigrationDto(item.getId(), item.getText(), item.getBook().getId());
        } catch (Exception e) {
            throw new Exception("Exception in CommentProcessor process()", e);
        }
    }
}
