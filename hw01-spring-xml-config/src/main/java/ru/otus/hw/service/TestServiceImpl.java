package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов

        List<Question> questionList = csvQuestionDao.findAll();

        for (int i = 0; i < questionList.size(); i++) {
            Question currentQuestion = questionList.get(i);
            List<Answer> answersForTheCurrentQuestion = currentQuestion.answers();
            ioService.printLine("");
            ioService.printFormattedLine("Question #%s: %s", i + 1, currentQuestion.text());

            if (answersForTheCurrentQuestion != null) {
                for (int j = 0; j < answersForTheCurrentQuestion.size(); j++) {
                    ioService.printFormattedLine("Answer #%s: %s",
                            j + 1, answersForTheCurrentQuestion.get(j).text());
                }
            } else {
                ioService.printLine("No answers for the current question");
            }
        }
    }
}
