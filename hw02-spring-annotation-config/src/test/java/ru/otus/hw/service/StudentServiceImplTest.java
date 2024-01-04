package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.impl.StreamsIOService;
import ru.otus.hw.service.impl.StudentServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@DisplayName("The test StudentServiceImpl should ")
@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    @Mock
    private IOService ioService;

    @BeforeEach
    void setUp() {
        ioService = mock(StreamsIOService.class);
    }

    @Test
    @DisplayName("Correct determinate student. current method: determineCurrentStudent()")
    void determineCurrentStudentTest() {
        Student expectedStudent = new Student("testFirstName", "testLastName");

        Mockito.when(ioService.readStringWithPrompt("Input your first name: ")).thenReturn("testFirstName");
        Mockito.when(ioService.readStringWithPrompt("Input your last name: ")).thenReturn("testLastName");
        StudentService studentService = new StudentServiceImpl(ioService);
        Student actual = studentService.determineCurrentStudent();

        assertEquals(expectedStudent, actual);
    }
}