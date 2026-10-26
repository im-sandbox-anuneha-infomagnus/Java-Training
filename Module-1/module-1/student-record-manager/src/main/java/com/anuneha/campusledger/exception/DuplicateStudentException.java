
package com.anuneha.campusledger.exception;

public class DuplicateStudentException extends RuntimeException {

    public DuplicateStudentException(int studentId) {
        super("Student ID " + studentId + " already exists.");
    }
}
