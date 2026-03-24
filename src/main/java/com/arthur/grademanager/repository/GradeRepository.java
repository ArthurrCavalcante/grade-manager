package com.arthur.grademanager.repository;

import com.arthur.grademanager.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT AVG(g.value) FROM Grade g WHERE g.student.id = :studentId")
    Optional<Double> findAverageByStudentId(@Param("studentId") Long studentId);
}
