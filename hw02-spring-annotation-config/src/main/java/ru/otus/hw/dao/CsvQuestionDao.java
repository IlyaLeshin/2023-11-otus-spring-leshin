package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.extractor.QuestionExtractorCsvFromResource;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final QuestionExtractorCsvFromResource questionExtractorCsvFromResource;

    @Override
    public List<Question> findAll() {
        return questionExtractorCsvFromResource.loadData();
    }
}