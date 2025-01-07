package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MigrationGenreIdCache implements MigrationIdCache<Long> {

    private final Queue<Long> genreIds = new ConcurrentLinkedQueue<>();

    @Override
    public void putList(List<Long> migrationGenreIdList) {
        genreIds.addAll(migrationGenreIdList);
    }

    @Override
    public Long getNext() {
        return genreIds.poll();
    }
}