package com.hona.module_32.service;

import com.hona.module_32.exception.DuplicateEmailException;
import com.hona.module_32.exception.StudentNotFoundException;
import com.hona.module_32.model.Student;
import com.hona.module_32.model.StudentRequest;
import com.hona.module_32.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentRequest request;

    @BeforeEach
    void setUp() {
        request = new StudentRequest();
        request.setName("Anu");
        request.setEmail("anu@example.com");
    }
    @Test
    void createStudent_whenValidRequest_returnsStudent() {
        Student savedStudent = new Student(1L, "Anu", "anu@example.com");

        when(studentRepository.findAll()).thenReturn(List.of());
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.createStudent(request);

        assertEquals(1L, result.getId());
        assertEquals("Anu", result.getName());
        assertEquals("anu@example.com", result.getEmail());
    }
    @Test
    void createStudent_whenEmailAlreadyExists_throwsException() {
        Student existingStudent = new Student(
                1L, "Rahul", "anu@example.com"
        );

        when(studentRepository.findAll())
                .thenReturn(List.of(existingStudent));

        assertThrows(
                DuplicateEmailException.class,
                () -> studentService.createStudent(request)
        );

        verify(studentRepository, never()).save(any(Student.class));
    }
    @Test
    void getStudent_whenStudentExists_returnsStudent() {
        Student student = new Student(1L, "Anu", "anu@example.com");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Student result = studentService.getStudent(1L);

        assertEquals(1L, result.getId());
        assertEquals("Anu", result.getName());
        assertEquals("anu@example.com", result.getEmail());
    }
    @Test
    void getStudent_whenStudentDoesNotExist_throwsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudent(99L)
        );
    }
    @Test
    void getAllStudents_returnsStudentList() {
        List<Student> students = List.of(
                new Student(1L, "Anu", "anu@example.com"),
                new Student(2L, "Rahul", "rahul@example.com")
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("Anu", result.get(0).getName());
        assertEquals("Rahul", result.get(1).getName());
    }
    @Test
    void updateStudent_whenStudentExists_updatesStudent() {
        Student existingStudent = new Student(
                1L, "Anu", "old@example.com"
        );

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.findAll())
                .thenReturn(List.of(existingStudent));

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Student result = studentService.updateStudent(1L, request);

        assertEquals("Anu", result.getName());
        assertEquals("anu@example.com", result.getEmail());
        verify(studentRepository).save(existingStudent);
    }
    @Test
    void updateStudent_whenStudentExists_updatesStudent1() {
        Student existingStudent = new Student(
                1L, "Anu", "old@example.com"
        );

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.findAll())
                .thenReturn(List.of(existingStudent));

        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Student result = studentService.updateStudent(1L, request);

        assertEquals("Anu", result.getName());
        assertEquals("anu@example.com", result.getEmail());
        verify(studentRepository).save(existingStudent);
    }
    @Test
    void deleteStudent_whenStudentExists_deletesStudent() {
        Student student = new Student(1L, "Anu", "anu@example.com");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }
    @Test
    void deleteStudent_whenStudentDoesNotExist_throwsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deleteStudent(99L)
        );

        verify(studentRepository, never()).deleteById(99L);
    }
}