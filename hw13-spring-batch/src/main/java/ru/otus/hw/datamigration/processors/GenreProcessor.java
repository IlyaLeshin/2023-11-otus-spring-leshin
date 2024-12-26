package ru.otus.hw.datamigration.processors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.cache.MigrationGenreCache;
import ru.otus.hw.datamigration.models.MigrationGenre;
import ru.otus.hw.datamigration.repositories.MigrationGenreRepository;
import ru.otus.hw.models.Genre;

@Component
@AllArgsConstructor
public class GenreProcessor implements ItemProcessor<Genre, MigrationGenre> {
    private final MigrationGenreCache migrationGenreCache;

    private final MigrationGenreRepository migrationGenreRepository;

    @Override
    public MigrationGenre process(@NonNull Genre item) throws Exception {
        try {
            String genreMongoId = item.getId();
            long genreSqlId = getSqlId();
            MigrationGenre migrationGenre = new MigrationGenre(genreSqlId, item.getName());
            migrationGenreCache.put(genreMongoId, migrationGenre);
            return migrationGenre;
        } catch (Exception e) {
            throw new Exception("Exception in GenreProcessor process()", e);
        }
    }

    private Long getSqlId() {
        return migrationGenreRepository.getNextSequenceId();
    }
}