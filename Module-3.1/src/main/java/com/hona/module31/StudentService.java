package com.hona.module31;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student getStudent(int id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Student not found: " + id
                        )
                );
    }

    public Student addStudent(Student student) {
        return repository.save(student);
    }
}