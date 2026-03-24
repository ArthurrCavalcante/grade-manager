package com.arthur.grademanager.service;

import com.arthur.grademanager.dto.GradeDTO;
import com.arthur.grademanager.dto.StudentDTO;
import com.arthur.grademanager.dto.CourseDTO;
import com.arthur.grademanager.exception.BusinessException;
import com.arthur.grademanager.mapper.GradeMapper;
import com.arthur.grademanager.model.Course;
import com.arthur.grademanager.model.Grade;
import com.arthur.grademanager.model.Student;
import com.arthur.grademanager.repository.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GradeService")
class GradeServiceTest {

    @Mock private GradeRepository gradeRepository;
    @Mock private StudentService studentService;
    @Mock private CourseService courseService;
    @Mock private GradeMapper gradeMapper;

    @InjectMocks
    private GradeService gradeService;

    private Student student;
    private Course course;
    private Grade grade;
    private GradeDTO.Request request;
    private GradeDTO.Response response;

    @BeforeEach
    void setUp() {
        student  = Student.builder().name("Arthur").registration("2024001").email("arthur@test.com").build();
        course   = Course.builder().name("Algorithms").code("ADS101").workload(80).build();
        grade    = Grade.builder().student(student).course(course).value(9.0).date(LocalDate.now()).build();
        request  = new GradeDTO.Request(1L, 1L, 9.0, LocalDate.now());
        response = new GradeDTO.Response(
                1L,
                new StudentDTO.Summary(1L, "Arthur", "2024001"),
                new CourseDTO.Response(1L, "Algorithms", "ADS101", 80),
                9.0, LocalDate.now(), true
        );
    }

    @Nested
    @DisplayName("create")
    class Create {
        @Test
        @DisplayName("should create grade successfully")
        void shouldCreate() {
            when(studentService.getStudentOrThrow(1L)).thenReturn(student);
            when(courseService.getCourseOrThrow(1L)).thenReturn(course);
            when(gradeRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
            when(gradeRepository.save(any())).thenReturn(grade);
            when(gradeMapper.toResponse(grade)).thenReturn(response);

            GradeDTO.Response result = gradeService.create(request);

            assertThat(result).isEqualTo(response);
            assertThat(result.approved()).isTrue();
            verify(gradeRepository).save(any(Grade.class));
        }

        @Test
        @DisplayName("should throw BusinessException on duplicate student/course pair")
        void shouldThrowOnDuplicate() {
            when(studentService.getStudentOrThrow(1L)).thenReturn(student);
            when(courseService.getCourseOrThrow(1L)).thenReturn(course);
            when(gradeRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

            assertThatThrownBy(() -> gradeService.create(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("already has a grade");
        }
    }

    @Nested
    @DisplayName("getStudentReport")
    class StudentReport {
        @Test
        @DisplayName("should return report with correct averages and counts")
        void shouldReturnReport() {
            Grade g1 = Grade.builder().student(student).course(course).value(8.0).date(LocalDate.now()).build();
            Grade g2 = Grade.builder().student(student).course(course).value(4.0).date(LocalDate.now()).build();

            StudentDTO.Summary summary = new StudentDTO.Summary(1L, "Arthur", "2024001");

            when(studentService.getStudentOrThrow(1L)).thenReturn(student);
            when(gradeRepository.findByStudentId(1L)).thenReturn(List.of(g1, g2));
            when(gradeMapper.toStudentSummary(student)).thenReturn(summary);

            GradeDTO.StudentReport report = gradeService.getStudentReport(1L);

            assertThat(report.average()).isEqualTo(6.0);
            assertThat(report.approvedCount()).isEqualTo(1);
            assertThat(report.failedCount()).isEqualTo(1);
            assertThat(report.grades()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Grade approval logic")
    class ApprovalLogic {
        @Test
        @DisplayName("grade >= 6.0 should be approved")
        void shouldBeApproved() {
            Grade g = Grade.builder().student(student).course(course).value(6.0).date(LocalDate.now()).build();
            assertThat(g.isApproved()).isTrue();
        }

        @Test
        @DisplayName("grade < 6.0 should be failed")
        void shouldBeFailed() {
            Grade g = Grade.builder().student(student).course(course).value(5.9).date(LocalDate.now()).build();
            assertThat(g.isApproved()).isFalse();
        }
    }
}
