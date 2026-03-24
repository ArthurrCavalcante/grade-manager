package com.arthur.grademanager.service;

import com.arthur.grademanager.dto.GradeDTO;
import com.arthur.grademanager.exception.BusinessException;
import com.arthur.grademanager.exception.ResourceNotFoundException;
import com.arthur.grademanager.mapper.GradeMapper;
import com.arthur.grademanager.model.Course;
import com.arthur.grademanager.model.Grade;
import com.arthur.grademanager.model.Student;
import com.arthur.grademanager.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final GradeMapper gradeMapper;

    @Transactional(readOnly = true)
    public List<GradeDTO.Response> findAll() {
        return gradeRepository.findAll().stream()
                .map(gradeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GradeDTO.Response findById(Long id) {
        return gradeMapper.toResponse(getGradeOrThrow(id));
    }

    @Transactional(readOnly = true)
    public GradeDTO.StudentReport getStudentReport(Long studentId) {
        Student student = studentService.getStudentOrThrow(studentId);
        List<Grade> grades = gradeRepository.findByStudentId(studentId);

        List<GradeDTO.ReportItem> items = grades.stream()
                .map(g -> new GradeDTO.ReportItem(
                        g.getCourse().getName(),
                        g.getCourse().getCode(),
                        g.getValue(),
                        g.isApproved()
                ))
                .toList();

        double average = grades.stream()
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0.0);

        long approved = grades.stream().filter(Grade::isApproved).count();
        long failed = grades.size() - approved;

        return new GradeDTO.StudentReport(
                gradeMapper.toStudentSummary(student),
                items,
                Math.round(average * 100.0) / 100.0,
                approved,
                failed
        );
    }

    @Transactional
    public GradeDTO.Response create(GradeDTO.Request request) {
        Student student = studentService.getStudentOrThrow(request.studentId());
        Course course   = courseService.getCourseOrThrow(request.courseId());

        if (gradeRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw new BusinessException(
                "Student " + student.getName() + " already has a grade for course " + course.getName()
            );
        }

        Grade grade = Grade.builder()
                .student(student)
                .course(course)
                .value(request.value())
                .date(request.date())
                .build();

        return gradeMapper.toResponse(gradeRepository.save(grade));
    }

    @Transactional
    public GradeDTO.Response update(Long id, GradeDTO.Request request) {
        Grade grade = getGradeOrThrow(id);

        // Allow update only if it's the same student/course pair, or no conflict exists
        boolean conflict = gradeRepository.findByStudentIdAndCourseId(request.studentId(), request.courseId())
                .map(g -> !g.getId().equals(id))
                .orElse(false);
        if (conflict) {
            throw new BusinessException("A grade already exists for this student/course combination.");
        }

        Student student = studentService.getStudentOrThrow(request.studentId());
        Course course   = courseService.getCourseOrThrow(request.courseId());

        grade.setStudent(student);
        grade.setCourse(course);
        grade.setValue(request.value());
        grade.setDate(request.date());

        return gradeMapper.toResponse(gradeRepository.save(grade));
    }

    @Transactional
    public void delete(Long id) {
        getGradeOrThrow(id);
        gradeRepository.deleteById(id);
    }

    private Grade getGradeOrThrow(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", id));
    }
}
