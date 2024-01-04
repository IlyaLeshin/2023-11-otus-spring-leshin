package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.loader.Loader;
import ru.otus.hw.dao.loader.ResourceLoader;
import ru.otus.hw.dao.parser.Parser;
import ru.otus.hw.dao.parser.ParserCsv;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("The test CscQuestionDao should ")
@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @Test
    @DisplayName("find all questions and answers from resource. current method: findAll()")
    void findAllTest() {
        List<Answer> answersForQuestionOne = new ArrayList<>();
        List<Answer> answersForQuestionTwo = new ArrayList<>();

        answersForQuestionOne.add(new Answer("Answer #1 for question #1", true));
        answersForQuestionOne.add(new Answer("Answer #2 for question #1", false));
        answersForQuestionOne.add(new Answer("Answer #3 for question #1", false));

        answersForQuestionTwo.add(new Answer("Answer #1 for question #2", false));
        answersForQuestionTwo.add(new Answer("Answer #2 for question #2", true));

        Question questionOne = new Question("Question #1", answersForQuestionOne);
        Question questionTwo = new Question("Question #2", answersForQuestionTwo);

        Question[] expectedQuestionArr = new Question[]{questionOne, questionTwo};

        Parser parserCsv = new ParserCsv();
        Loader resourceLoader = new ResourceLoader();
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(resourceLoader, parserCsv, testFileNameProvider);
        Mockito.when(testFileNameProvider.getTestFileName()).thenReturn("test-questions.csv");
        List<Question> actualQuestionList = csvQuestionDao.findAll();

        assertArrayEquals(expectedQuestionArr, actualQuestionList.toArray());
    }
}