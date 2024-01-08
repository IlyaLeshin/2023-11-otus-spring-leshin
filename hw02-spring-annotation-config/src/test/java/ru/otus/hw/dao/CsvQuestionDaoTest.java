package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.extractor.QuestionExtractorCsvFromResource;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("The test CsvQuestionDao should ")
@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private QuestionExtractorCsvFromResource questionExtractorCsvFromResource;

    @Test
    @DisplayName("find all questions and answers from resource. current method: findAll()")
    void findAllTest() {
        Question[] expectedQuestionArr = getQuestionsArr();

        Mockito.when(questionExtractorCsvFromResource.loadData()).thenReturn(Arrays.stream(expectedQuestionArr).toList());
        List<Question> actualQuestionList = csvQuestionDao.findAll();

        assertArrayEquals(expectedQuestionArr, actualQuestionList.toArray());
    }

    private Question[] getQuestionsArr() {
        List<Answer> answersForQuestionOne = new ArrayList<>();
        List<Answer> answersForQuestionTwo = new ArrayList<>();

        answersForQuestionOne.add(new Answer("Answer #1 for question #1", true));
        answersForQuestionOne.add(new Answer("Answer #2 for question #1", false));
        answersForQuestionOne.add(new Answer("Answer #3 for question #1", false));

        answersForQuestionTwo.add(new Answer("Answer #1 for question #2", false));
        answersForQuestionTwo.add(new Answer("Answer #2 for question #2", true));

        Question questionOne = new Question("Question #1", answersForQuestionOne);
        Question questionTwo = new Question("Question #2", answersForQuestionTwo);

        return new Question[]{questionOne, questionTwo};
    }
}