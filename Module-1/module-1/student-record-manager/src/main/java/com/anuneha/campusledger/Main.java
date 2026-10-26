
package com.anuneha.campusledger;

import com.anuneha.campusledger.model.Student;
import com.anuneha.campusledger.service.StudentService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        StudentService service = new StudentService();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("\n========== CAMPUS LEDGER ==========");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Search Student");
                System.out.println("4. Remove Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> {
                            System.out.print("Student ID: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            System.out.print("Name: ");
                            String name = scanner.nextLine().trim();

                            System.out.print("Email: ");
                            String email = scanner.nextLine().trim();

                            System.out.print("Department: ");
                            String department = scanner.nextLine().trim();

                            System.out.print("Marks: ");
                            double marks = Double.parseDouble(scanner.nextLine());

                            Student student = new Student(
                                    id, name, email, department, marks
                            );

                            service.addStudent(student);
                            System.out.println("Student added successfully!");
                        }

                        case "2" -> {
                            var students = service.getAllStudents();

                            if (students.isEmpty()) {
                                System.out.println("No student records found.");
                            } else {
                                students.forEach(System.out::println);
                            }
                        }

                        case "3" -> {
                            System.out.print("Enter student ID: ");
                            int id = Integer.parseInt(scanner.nextLine());
                            System.out.println(service.findStudentById(id));
                        }

                        case "4" -> {
                            System.out.print("Enter student ID to remove: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            service.removeStudent(id);
                            System.out.println("Student removed successfully!");
                        }

                        case "5" -> {
                            running = false;
                            System.out.println("Thank you for using CampusLedger!");
                        }

                        default -> System.out.println(
                                "Invalid choice. Please select 1 to 5."
                        );
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Input error: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
            }
        }
    }
}
