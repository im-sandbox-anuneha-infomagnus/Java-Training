
package com.anuneha.campusledger.service;

import com.anuneha.campusledger.exception.DuplicateStudentException;
import com.anuneha.campusledger.exception.StudentNotFoundException;
import com.anuneha.campusledger.model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentService {

    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        boolean exists = students.stream()
                .anyMatch(s -> s.getId() == student.getId());

        if (exists) {
            throw new DuplicateStudentException(student.getId());
        }

        students.add(student);
    }

    public Student findStudentById(int id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public List<Student> getAllStudents() {
        return students.stream()
                .sorted(Comparator.comparingInt(Student::getId))
                .toList();
    }

    public boolean removeStudent(int id) {
        Student student = findStudentById(id);
        return students.remove(student);
    }
}
