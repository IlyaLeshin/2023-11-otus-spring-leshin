package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MigrationBookCache implements MigrationCache<String,Long> {
    private final ConcurrentHashMap<String, Long> booksIds = new ConcurrentHashMap<>();

    @Override
    public void put(String mongoId, Long migrationBookId) {
        booksIds.put(mongoId, migrationBookId);
    }

    @Override
    public Long get(String mongoId) {
        return booksIds.get(mongoId);
    }
}