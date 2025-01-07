package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MigrationBookIdCache implements MigrationIdCache<Long> {

    private final Queue<Long> booksIds = new ConcurrentLinkedQueue<>();

    @Override
    public void putList(List<Long> migrationBookIdList) {
        booksIds.addAll(migrationBookIdList);
    }

    @Override
    public Long getNext() {
        return booksIds.poll();
    }
}