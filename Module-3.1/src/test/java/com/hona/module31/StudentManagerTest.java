
package com.hona.module31;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentManagerTest {

    private StudentManager studentManager;

    @BeforeEach
    void setUp() {
        studentManager = new StudentManager();
    }

    @Test
    void addStudent_shouldStoreStudent() {

        // Arrange
        Student student = new Student(
                101, "Anu", "anu@gmail.com"
        );

        // Act
        studentManager.addStudent(student);

        // Assert
        Student result = studentManager.findStudentById(101);

        assertThat(result.getId()).isEqualTo(101);
        assertThat(result.getName()).isEqualTo("Anu");
        assertThat(result.getEmail()).isEqualTo("anu@gmail.com");
    }

    @Test
    void addStudent_shouldThrowException_whenIdAlreadyExists() {

        // Arrange
        Student student1 = new Student(101, "Anu", "anu@gmail.com");
        Student student2 = new Student(101, "Ravi", "ravi@gmail.com");

        studentManager.addStudent(student1);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> studentManager.addStudent(student2)
        );
    }

    @Test
    void findStudentById_shouldThrowException_whenStudentNotFound() {

        // Arrange
        int studentId = 999;

        // Act & Assert
        assertThrows(
                NoSuchElementException.class,
                () -> studentManager.findStudentById(studentId)
        );
    }

    @Test
    void updateStudent_shouldUpdateStudentDetails() {

        // Arrange
        Student student = new Student(
                101, "Anu", "anu@gmail.com"
        );

        studentManager.addStudent(student);

        // Act
        studentManager.updateStudent(
                101, "Anu Peteti", "newanu@gmail.com"
        );

        // Assert
        Student updatedStudent =
                studentManager.findStudentById(101);

        assertThat(updatedStudent.getName()).isEqualTo("Anu Peteti");
        assertThat(updatedStudent.getEmail()).isEqualTo("newanu@gmail.com");
    }

    @Test
    void updateStudent_shouldThrowException_whenStudentNotFound() {

        // Arrange
        int studentId = 999;

        // Act & Assert
        assertThrows(
                NoSuchElementException.class,
                () -> studentManager.updateStudent(
                        studentId, "Anu", "anu@gmail.com"
                )
        );
    }

    @Test
    void deleteStudent_shouldRemoveStudent_whenIdExists() {

        // Arrange
        Student student = new Student(
                101, "Anu", "anu@gmail.com"
        );

        studentManager.addStudent(student);

        // Act
        studentManager.deleteStudent(101);

        // Assert
        assertThrows(
                NoSuchElementException.class,
                () -> studentManager.findStudentById(101)
        );
    }

    @Test
    void deleteStudent_shouldThrowException_whenStudentNotFound() {

        // Arrange
        int studentId = 999;

        // Act & Assert
        assertThrows(
                NoSuchElementException.class,
                () -> studentManager.deleteStudent(studentId)
        );
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {

        // Arrange
        Student student1 = new Student(101, "Anu", "anu@gmail.com");
        Student student2 = new Student(102, "Ravi", "ravi@gmail.com");

        studentManager.addStudent(student1);
        studentManager.addStudent(student2);

        // Act
        List<Student> result = studentManager.getAllStudents();

        // Assert
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(student1, student2);
    }

    @Test
    void getAllStudents_shouldReturnEmptyList_whenNoStudentsExist() {

        // Act
        List<Student> result = studentManager.getAllStudents();

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }
}
