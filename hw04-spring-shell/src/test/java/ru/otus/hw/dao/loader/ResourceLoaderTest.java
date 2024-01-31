package ru.otus.hw.dao.loader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("The test ResourceLoader should ")
public class ResourceLoaderTest {

    @Test
    @DisplayName("load rowData from resource. current method: loadData(String dataSource)")
    void resourceLoaderTest() {
        String separator = System.lineSeparator();
        String expectedRowData = "# SkipLines;" + separator +
                "Question #1;Answer #1 for question #1%true|Answer #2 for question #1%false|Answer #3 for question #1%false" + separator +
                "Question #2;Answer #1 for question #2%false|Answer #2 for question #2%true";

        Loader resourceLoader = new ResourceLoader();
        String actualRowData = resourceLoader.loadData("test-questions.csv");

        assertEquals(expectedRowData, actualRowData);
    }
}