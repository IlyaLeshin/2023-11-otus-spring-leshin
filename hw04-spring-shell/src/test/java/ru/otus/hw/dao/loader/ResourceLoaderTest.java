package ru.otus.hw.dao.loader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("The test ResourceLoader should ")
@SpringBootTest(classes = ResourceLoader.class)
public class ResourceLoaderTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @DisplayName("load rowData from resource. current method: loadData(String dataSource)")
    void resourceLoaderTest() {
        String separator = System.lineSeparator();
        String expectedRowData = "# SkipLines;" + separator +
                "Question #1;Answer #1 for question #1%true|Answer #2 for question #1%false|Answer #3 for question #1%false" + separator +
                "Question #2;Answer #1 for question #2%false|Answer #2 for question #2%true";

        String actualRowData = resourceLoader.loadData("test-questions_en_US.csv");

        assertEquals(expectedRowData, actualRowData);
    }
}