package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.cache.MigrationAuthorCache;
import ru.otus.hw.datamigration.cache.MigrationBookCache;


import ru.otus.hw.datamigration.cache.MigrationBookIdCache;
import ru.otus.hw.datamigration.cache.MigrationGenreCache;
import ru.otus.hw.datamigration.models.MigrationBook;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@Component
@AllArgsConstructor
public class BookProcessor implements ItemProcessor<Book, MigrationBook> {

    private final MigrationAuthorCache migrationAuthorCache;

    private final MigrationGenreCache migrationGenreCache;

    private final MigrationBookCache migrationBookCache;

    private final MigrationBookIdCache migrationBookIdCache;

    @Override
    public MigrationBook process(@NonNull Book item) throws Exception {
        try {
            String bookMongoId = item.getId();
            long bookSqlId = getSqlId();
            String authorMongoId = item.getAuthor().getId();
            List<String> genresMongoIds = item.getGenres().stream().map(Genre::getId).toList();
            migrationBookCache.put(bookMongoId, bookSqlId);

            return new MigrationBook(bookSqlId, item.getTitle(),
                    migrationAuthorCache.get(authorMongoId),
                    genresMongoIds.stream().map(migrationGenreCache::get).toList());

        } catch (Exception e) {
            throw new Exception("Exception in MigrationBook process", e);
        }
    }

    private Long getSqlId() {
        return migrationBookIdCache.getNext();
    }
}
