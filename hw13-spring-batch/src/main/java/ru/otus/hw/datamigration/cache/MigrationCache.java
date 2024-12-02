package ru.otus.hw.datamigration.cache;

public interface MigrationCache<K,T> {

    void put(K key, T value);

    T get(K key);
}