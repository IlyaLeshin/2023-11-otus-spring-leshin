package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;
import ru.otus.hw.datamigration.models.MigrationAuthor;


import java.util.concurrent.ConcurrentHashMap;

@Component
public class MigrationAuthorCache implements MigrationCache<String,MigrationAuthor> {
    private final ConcurrentHashMap<String, MigrationAuthor> authors = new ConcurrentHashMap<>();

    @Override
    public void put(String mongoId, MigrationAuthor migrationAuthor) {
        authors.put(mongoId, migrationAuthor);
    }

    @Override
    public MigrationAuthor get(String mongoId) {
        return authors.get(mongoId);
    }
}