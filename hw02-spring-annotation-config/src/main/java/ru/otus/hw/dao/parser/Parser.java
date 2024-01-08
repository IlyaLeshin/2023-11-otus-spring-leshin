package ru.otus.hw.dao.parser;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface Parser {
    List<Question> parse(String rawData);
}