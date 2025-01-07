package ru.otus.hw.datamigration.cache;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MigrationCommentIdCache implements MigrationIdCache<Long> {

    private final Queue<Long> commentsIds = new ConcurrentLinkedQueue<>();

    @Override
    public void putList(List<Long> migrationCommentIdList) {
        commentsIds.addAll(migrationCommentIdList);
    }

    @Override
    public Long getNext() {
        return commentsIds.poll();
    }
}