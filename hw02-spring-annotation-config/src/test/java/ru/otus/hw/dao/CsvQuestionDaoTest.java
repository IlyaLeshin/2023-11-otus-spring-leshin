package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;

@DisplayName("The test CscQuestionDao should ")
@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {
        fileNameProvider = mock(TestFileNameProvider.class);
    }

    @Test
    @DisplayName("find all questions and answers from resource. current method: findAll()")
    void executeTestTest() {
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn("test-questions.csv");
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(fileNameProvider);

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

        List<Question> actualQuestionList = csvQuestionDao.findAll();

        assertArrayEquals(expectedQuestionArr, actualQuestionList.toArray());
    }
}
