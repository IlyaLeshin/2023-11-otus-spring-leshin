package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.cache.MigrationBookCache;
import ru.otus.hw.datamigration.dto.MigrationCommentDto;
import ru.otus.hw.models.Comment;

import java.util.concurrent.atomic.AtomicLong;

@Component
@AllArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, MigrationCommentDto> {
    private final MigrationBookCache migrationBookCache;

    private final AtomicLong commentIdSequence = new AtomicLong();

    @Override
    public MigrationCommentDto process(@NonNull Comment item) throws Exception {
        try {
            Long commentSqlId = getSqlId();
            String bookMongoId = item.getBook().getId();
            long bookSqlId = migrationBookCache.get(bookMongoId);

            return new MigrationCommentDto(commentSqlId, item.getText(), bookSqlId);
        } catch (Exception e) {
            throw new Exception("Exception in CommentProcessor process()", e);
        }
    }


    private Long getSqlId() {
        return commentIdSequence.incrementAndGet();
    }
}
