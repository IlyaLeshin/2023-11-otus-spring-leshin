package ru.otus.hw.dao.loader;

import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceLoader implements Loader {

    @Override
    public String loadData(String dataSource) {
        try (InputStream inputStream = getFileFromResource(dataSource);
             InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {

            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));

        } catch (IOException | URISyntaxException e) {
            throw new QuestionReadException("error reading a file from resources", e);
        }
    }

    private InputStream getFileFromResource(String fileName) throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found !" + fileName);
        } else {
            return resource.openConnection().getInputStream();
        }
    }
}