
package com.anuneha.campusledger.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(int studentId) {
        super("Student with ID " + studentId + " was not found.");
    }
}
