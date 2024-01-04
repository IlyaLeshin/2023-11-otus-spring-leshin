package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionNotFondException;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestService;


@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            printCurrentQuestion(question);
            printAnswersToTheCurrentQuestion(question);
            var isAnswerValid = getStudentAnswerToTheCurrentQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }

        return testResult;
    }

    private void printCurrentQuestion(Question question) {
        try {
            ioService.printLine("\n" + question.text());
        } catch (NullPointerException e) {
            throw new QuestionNotFondException("question not found", e);
        }
    }

    private void printAnswersToTheCurrentQuestion(Question question) {
        try {
            var answersToTheCurrentQuestion = question.answers();

            if (answersToTheCurrentQuestion != null) {
                for (int i = 0; i < answersToTheCurrentQuestion.size(); i++) {
                    ioService.printFormattedLine("Answer #%s: %s",
                            i + 1, answersToTheCurrentQuestion.get(i).text());
                }
            } else {
                ioService.printLine("No answers for the current question");
            }
        } catch (NullPointerException e) {
            throw new QuestionNotFondException("question not found", e);
        }
    }

    private boolean getStudentAnswerToTheCurrentQuestion(Question question) {
        try {
            var minNumberOfAvailableAnswers = 1;
            var maxNumberOfAvailableAnswers = question.answers().size();
            int studentAnswerNumber = ioService.readIntForRangeWithPrompt(minNumberOfAvailableAnswers,
                    maxNumberOfAvailableAnswers,
                    "Enter your answer number:",
                    "Your answer number is outside the range of available answers, try again:");

            return question.answers().get(studentAnswerNumber - 1).isCorrect();
        } catch (NullPointerException e) {
            throw new QuestionNotFondException("question not found", e);
        }
    }
}