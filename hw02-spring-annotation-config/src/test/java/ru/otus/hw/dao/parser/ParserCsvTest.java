package ru.otus.hw.dao.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@DisplayName("The test CscQuestionDao should ")
public class ParserCsvTest {

    @Test
    @DisplayName("find all questions and answers from resource. current method: findAll()")
    void parserCsvTest() {
        Parser parserCsv = new ParserCsv();
        String separator = System.lineSeparator();
        String rowData = "# SkipLines;" + separator +
                "Question #1;Answer #1 for question #1%true|Answer #2 for question #1%false|Answer #3 for question #1%false" + separator +
                "Question #2;Answer #1 for question #2%false|Answer #2 for question #2%true";

        List<Question> actualQuestionList = parserCsv.parse(rowData);


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

          assertArrayEquals(expectedQuestionArr, actualQuestionList.toArray());
    }
}