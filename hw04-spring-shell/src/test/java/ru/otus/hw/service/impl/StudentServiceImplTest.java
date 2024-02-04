package ru.otus.hw.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("The test StudentServiceImpl should ")
@SpringBootTest(classes = StudentServiceImpl.class)
public class StudentServiceImplTest {

    @Autowired
    private StudentServiceImpl studentService;
    @MockBean
    private LocalizedIOService localizedIOService;

    @Test
    @DisplayName("Correct determinate student. current method: determineCurrentStudent()")
    void determineCurrentStudentTest() {
        Student expectedStudent = new Student("testFirstName", "testLastName");

        when(localizedIOService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn("testFirstName");
        when(localizedIOService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn("testLastName");

        Student actual = studentService.determineCurrentStudent();

        assertEquals(expectedStudent, actual);
    }
}