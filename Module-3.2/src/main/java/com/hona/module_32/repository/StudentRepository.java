package com.hona.module_32.repository;

import com.hona.module_32.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findById(Long id);

    List<Student> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);
}
