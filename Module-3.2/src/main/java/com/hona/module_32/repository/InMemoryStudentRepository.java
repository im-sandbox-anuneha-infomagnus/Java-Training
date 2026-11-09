package com.hona.module_32.repository;

import com.hona.module_32.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryStudentRepository implements StudentRepository {

    private final Map<Long, Student> students = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Student save(Student student) {
        if (student.getId() == null) {
            student.setId(idGenerator.incrementAndGet());
        }

        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public boolean existsById(Long id) {
        return students.containsKey(id);
    }

    @Override
    public void deleteById(Long id) {
        students.remove(id);
    }
}
