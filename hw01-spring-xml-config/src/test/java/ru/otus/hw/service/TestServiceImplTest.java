package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.testutils.StreamsIOServiceForTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@DisplayName("The test TestServiceImpl should ")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    public void setUp() {
        csvQuestionDao = mock(CsvQuestionDao.class);
    }

    @Test
    @DisplayName("output questions and answers. current method: executeTest()")
    void executeTestTest() {

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        String expectedString = new String(System.lineSeparator() +
                "Please answer the questions below" + System.lineSeparator() +
                System.lineSeparator() +
                System.lineSeparator() +
                "Question #1: Question #1" + System.lineSeparator() +
                "Answer #1: Answer #1 for question #1" + System.lineSeparator() +
                "Answer #2: Answer #2 for question #1" + System.lineSeparator() +
                "Answer #3: Answer #3 for question #1" + System.lineSeparator() +
                System.lineSeparator() +
                "Question #2: Question #2" + System.lineSeparator() +
                "Answer #1: Answer #1 for question #2" + System.lineSeparator() +
                "Answer #2: Answer #2 for question #2" +
                System.lineSeparator());

        List<Answer> answersForQuestionOne = new ArrayList<>();
        answersForQuestionOne.add(new Answer("Answer #1 for question #1", true));
        answersForQuestionOne.add(new Answer("Answer #2 for question #1", false));
        answersForQuestionOne.add(new Answer("Answer #3 for question #1", false));

        List<Answer> answersForQuestionTwo = new ArrayList<>();
        answersForQuestionTwo.add(new Answer("Answer #1 for question #2", false));
        answersForQuestionTwo.add(new Answer("Answer #2 for question #2", true));

        Question questionOne = new Question("Question #1", answersForQuestionOne);
        Question questionTwo = new Question("Question #2", answersForQuestionTwo);

        Question[] QuestionArrForActualString = new Question[]{questionOne, questionTwo};

        Mockito.when(csvQuestionDao.findAll()).thenReturn(Arrays.stream(QuestionArrForActualString).toList());
        IOService ioServiceForTest = new StreamsIOServiceForTest(new PrintStream(outContent));
        TestService actualString = new TestServiceImpl(ioServiceForTest, csvQuestionDao);

        actualString.executeTest();

        assertEquals(expectedString, outContent.toString());
    }
}