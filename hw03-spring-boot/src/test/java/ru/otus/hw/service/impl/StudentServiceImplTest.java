package ru.otus.hw.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.impl.StudentServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("The test StudentServiceImpl should ")
@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;
    @Mock
    private LocalizedIOService localizedIOService;

    @Test
    @DisplayName("Correct determinate student. current method: determineCurrentStudent()")
    void determineCurrentStudentTest() {
        Student expectedStudent = new Student("testFirstName", "testLastName");

        Mockito.when(localizedIOService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn("testFirstName");
        Mockito.when(localizedIOService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn("testLastName");

        Student actual = studentService.determineCurrentStudent();

        assertEquals(expectedStudent, actual);
    }
}