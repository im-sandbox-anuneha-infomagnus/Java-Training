package com.hona.module31;

import java.util.Optional;

public interface StudentRepository {

    Optional<Student> findById(int id);

    Student save(Student student);
}