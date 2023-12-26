package ru.otus.hw.dao.extractor;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionExtractor {
    List<Question> loadData();
    }
