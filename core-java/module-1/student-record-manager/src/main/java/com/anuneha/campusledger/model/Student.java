
package com.anuneha.campusledger.model;

public class Student {

    private final int id;
    private final String name;
    private final String email;
    private final String department;
    private final double marks;

    public Student(int id, String name, String email,
               String department, double marks) {

    if (id <= 0) {
        throw new IllegalArgumentException("Student ID must be positive.");
    }

    if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Name cannot be empty.");
    }

    if (email == null || !email.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
        throw new IllegalArgumentException("Please enter a valid email.");
    }

    if (department == null || department.isBlank()) {
        throw new IllegalArgumentException("Department cannot be empty.");
    }

    if (!Double.isFinite(marks) || marks < 0 || marks > 100) {
        throw new IllegalArgumentException("Marks must be between 0 and 100.");
    }

    this.id = id;
    this.name = name.trim();
    this.email = email.trim();
    this.department = department.trim();
    this.marks = marks;
}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public double getMarks() {
        return marks;
    }
    
    @Override
    public String toString() {
        return "Student {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", marks=" + marks +
                '}';
    }

}
