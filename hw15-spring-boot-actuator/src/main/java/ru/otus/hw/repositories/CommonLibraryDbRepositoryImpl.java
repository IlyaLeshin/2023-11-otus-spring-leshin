package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CommonLibraryDbRepositoryImpl implements CommonLibraryDbRepository {

    private final JdbcOperations jdbcTemplate;

    @Override
    public void executeTestQuery() {
        jdbcTemplate.execute("select 1 from duals");
    }
}
