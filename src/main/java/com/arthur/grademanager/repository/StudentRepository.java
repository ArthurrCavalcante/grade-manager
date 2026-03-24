package com.arthur.grademanager.repository;

import com.arthur.grademanager.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRegistration(String registration);
    Optional<Student> findByEmail(String email);
    boolean existsByRegistration(String registration);
    boolean existsByEmail(String email);
}
