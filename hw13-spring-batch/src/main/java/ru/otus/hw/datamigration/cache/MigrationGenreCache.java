package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.models.MigrationGenre;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MigrationGenreCache implements MigrationCache<String, MigrationGenre> {
    private final ConcurrentHashMap<String, MigrationGenre> genres = new ConcurrentHashMap<>();

    @Override
    public void put(String mongoId, MigrationGenre migrationGenre) {
        genres.put(mongoId, migrationGenre);
    }

    @Override
    public MigrationGenre get(String mongoId) {
        return genres.get(mongoId);
    }
}