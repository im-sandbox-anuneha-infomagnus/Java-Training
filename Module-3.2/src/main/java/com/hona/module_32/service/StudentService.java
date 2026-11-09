package com.hona.module_32.service;
import com.hona.module_32.exception.DuplicateEmailException;
import com.hona.module_32.exception.StudentNotFoundException;
import com.hona.module_32.model.Student;
import com.hona.module_32.model.StudentRequest;
import com.hona.module_32.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(StudentRequest request) {

        boolean emailExists = studentRepository.findAll()
                .stream()
                .anyMatch(student ->
                        student.getEmail().equalsIgnoreCase(request.getEmail()));

        if (emailExists) {
            throw new DuplicateEmailException("Student email already exists");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());

        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found with ID: " + id));
                
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(Long id, StudentRequest request) {
        Student existingStudent = getStudent(id);

        boolean emailExists = studentRepository.findAll()
                .stream()
                .anyMatch(student ->
                        !student.getId().equals(id)
                                && student.getEmail().equalsIgnoreCase(request.getEmail()));

        if (emailExists) {
            throw new IllegalArgumentException("Student email already exists");
        }

        existingStudent.setName(request.getName());
        existingStudent.setEmail(request.getEmail());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        getStudent(id);
        studentRepository.deleteById(id);
    }
}
