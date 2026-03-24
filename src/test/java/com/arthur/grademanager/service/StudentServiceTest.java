package com.arthur.grademanager.service;

import com.arthur.grademanager.dto.StudentDTO;
import com.arthur.grademanager.exception.BusinessException;
import com.arthur.grademanager.exception.ResourceNotFoundException;
import com.arthur.grademanager.mapper.StudentMapper;
import com.arthur.grademanager.model.Student;
import com.arthur.grademanager.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO.Request request;
    private StudentDTO.Response response;

    @BeforeEach
    void setUp() {
        student  = Student.builder().name("Arthur").registration("2024001").email("arthur@test.com").build();
        request  = new StudentDTO.Request("Arthur", "2024001", "arthur@test.com");
        response = new StudentDTO.Response(1L, "Arthur", "2024001", "arthur@test.com");
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {
        @Test
        @DisplayName("should return list of students")
        void shouldReturnList() {
            when(studentRepository.findAll()).thenReturn(List.of(student));
            when(studentMapper.toResponse(student)).thenReturn(response);

            List<StudentDTO.Response> result = studentService.findAll();

            assertThat(result).hasSize(1).contains(response);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {
        @Test
        @DisplayName("should return student when found")
        void shouldReturnStudent() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(studentMapper.toResponse(student)).thenReturn(response);

            assertThat(studentService.findById(1L)).isEqualTo(response);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when not found")
        void shouldThrowWhenNotFound() {
            when(studentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> studentService.findById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("create")
    class Create {
        @Test
        @DisplayName("should create student successfully")
        void shouldCreateStudent() {
            when(studentRepository.existsByRegistration(any())).thenReturn(false);
            when(studentRepository.existsByEmail(any())).thenReturn(false);
            when(studentRepository.save(any())).thenReturn(student);
            when(studentMapper.toResponse(student)).thenReturn(response);

            StudentDTO.Response result = studentService.create(request);

            assertThat(result).isEqualTo(response);
            verify(studentRepository).save(any(Student.class));
        }

        @Test
        @DisplayName("should throw BusinessException when registration already exists")
        void shouldThrowOnDuplicateRegistration() {
            when(studentRepository.existsByRegistration("2024001")).thenReturn(true);

            assertThatThrownBy(() -> studentService.create(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("2024001");
        }

        @Test
        @DisplayName("should throw BusinessException when email already exists")
        void shouldThrowOnDuplicateEmail() {
            when(studentRepository.existsByRegistration(any())).thenReturn(false);
            when(studentRepository.existsByEmail("arthur@test.com")).thenReturn(true);

            assertThatThrownBy(() -> studentService.create(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("arthur@test.com");
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {
        @Test
        @DisplayName("should delete student when exists")
        void shouldDelete() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            studentService.delete(1L);

            verify(studentRepository).deleteById(1L);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when student not found")
        void shouldThrowWhenNotFound() {
            when(studentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> studentService.delete(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
