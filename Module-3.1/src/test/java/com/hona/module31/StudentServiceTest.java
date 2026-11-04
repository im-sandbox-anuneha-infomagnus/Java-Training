package com.hona.module31;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getStudent_shouldReturnStudent_whenIdExists() {

        // Arrange
        Student student = new Student(
                101, "Anu", "anu@gmail.com"
        );

        when(repository.findById(101))
                .thenReturn(Optional.of(student));

        // Act
        Student result = studentService.getStudent(101);

        // Assert
        assertThat(result.getName()).isEqualTo("Anu");
        assertThat(result.getEmail()).isEqualTo("anu@gmail.com");
    }
}