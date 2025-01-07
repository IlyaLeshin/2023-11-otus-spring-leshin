package ru.otus.hw.datamigration.cache;

import java.util.List;

public interface MigrationIdCache<T> {

    void putList(List<T> value);

    T getNext();
}