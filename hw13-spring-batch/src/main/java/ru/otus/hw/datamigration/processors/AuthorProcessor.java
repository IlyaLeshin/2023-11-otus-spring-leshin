package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.cache.MigrationAuthorCache;
import ru.otus.hw.datamigration.cache.MigrationAuthorIdCache;
import ru.otus.hw.datamigration.models.MigrationAuthor;
import ru.otus.hw.models.Author;

@Component
@AllArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, MigrationAuthor>, StepExecutionListener {
    private final MigrationAuthorCache migrationAuthorCache;

    private final MigrationAuthorIdCache migrationAuthorIdCache;


    @Override
    public MigrationAuthor process(@NonNull Author item) throws Exception {
        try {
            String authorMongoId = item.getId();
            long authorSqlId = getSqlId();
            MigrationAuthor migrationAuthor = new MigrationAuthor(authorSqlId, item.getFullName());
            migrationAuthorCache.put(authorMongoId, migrationAuthor);

            return migrationAuthor;

        } catch (Exception e) {
            throw new Exception("Exception in AuthorProcessor process()", e);
        }
    }

    private Long getSqlId() {
        return migrationAuthorIdCache.getNext();
    }

}

