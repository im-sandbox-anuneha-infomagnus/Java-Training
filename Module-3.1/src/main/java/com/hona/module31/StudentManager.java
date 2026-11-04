package com.hona.module31;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class StudentManager {

    private final Map<Integer, Student> students = new HashMap<>();

    public void addStudent(Student student) {

        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException(
                    "Student ID already exists: " + student.getId()
            );
        }

        students.put(student.getId(), student);
    }
    public Student findStudentById(int id) {

        Student student = students.get(id);

        if (student == null) {
            throw new NoSuchElementException(
                    "Student not found with ID: " + id
            );
        }

        return student;
    }
    public void updateStudent(int id, String newName, String newEmail) {

        Student student = findStudentById(id);

        student.setName(newName);
        student.setEmail(newEmail);
    }
    public void deleteStudent(int id) {

        if (!students.containsKey(id)) {
            throw new NoSuchElementException(
                    "Student not found with ID: " + id
            );
        }

        students.remove(id);
    }
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
}