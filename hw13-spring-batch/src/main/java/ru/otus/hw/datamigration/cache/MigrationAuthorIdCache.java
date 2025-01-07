package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MigrationAuthorIdCache implements MigrationIdCache<Long> {

    private final Queue<Long> authorsIds = new ConcurrentLinkedQueue<>();

    @Override
    public void putList(List<Long> migrationAuthorIdList) {
        authorsIds.addAll(migrationAuthorIdList);
    }

    @Override
    public Long getNext() {
        return authorsIds.poll();
    }
}