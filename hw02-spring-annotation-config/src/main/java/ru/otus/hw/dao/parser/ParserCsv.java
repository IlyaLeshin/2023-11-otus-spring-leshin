package ru.otus.hw.dao.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

@Component
public class ParserCsv implements Parser {
    @Override
    public List<Question> parse(String rawData) {
        Reader rawDataReader = new StringReader(rawData);
        CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(rawDataReader)
                .withSkipLines(1)
                .withSeparator(';')
                .withType(QuestionDto.class)
                .build();

        List<Question> questionList = csvToBean
                .parse()
                .stream()
                .map(QuestionDto::toDomainObject)
                .toList();

        return questionList;
    }
}