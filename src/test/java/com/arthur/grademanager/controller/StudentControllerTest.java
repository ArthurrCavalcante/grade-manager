package com.arthur.grademanager.controller;

import com.arthur.grademanager.dto.StudentDTO;
import com.arthur.grademanager.exception.ResourceNotFoundException;
import com.arthur.grademanager.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@DisplayName("StudentController")
class StudentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean StudentService studentService;

    private final StudentDTO.Response response =
            new StudentDTO.Response(1L, "Arthur", "2024001", "arthur@test.com");

    @Nested
    @DisplayName("GET /api/v1/students")
    class GetAll {
        @Test
        @DisplayName("should return 200 with list of students")
        void shouldReturn200() throws Exception {
            when(studentService.findAll()).thenReturn(List.of(response));

            mockMvc.perform(get("/api/v1/students"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Arthur"))
                    .andExpect(jsonPath("$[0].registration").value("2024001"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/students/{id}")
    class GetById {
        @Test
        @DisplayName("should return 200 when student exists")
        void shouldReturn200() throws Exception {
            when(studentService.findById(1L)).thenReturn(response);

            mockMvc.perform(get("/api/v1/students/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email").value("arthur@test.com"));
        }

        @Test
        @DisplayName("should return 404 when student not found")
        void shouldReturn404() throws Exception {
            when(studentService.findById(99L))
                    .thenThrow(new ResourceNotFoundException("Student", 99L));

            mockMvc.perform(get("/api/v1/students/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Student not found with id: 99"));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/students")
    class Create {
        @Test
        @DisplayName("should return 201 when student is created")
        void shouldReturn201() throws Exception {
            StudentDTO.Request request =
                    new StudentDTO.Request("Arthur", "2024001", "arthur@test.com");
            when(studentService.create(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Arthur"));
        }

        @Test
        @DisplayName("should return 400 when request body is invalid")
        void shouldReturn400OnInvalidBody() throws Exception {
            StudentDTO.Request invalid = new StudentDTO.Request("", "", "not-an-email");

            mockMvc.perform(post("/api/v1/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.email").exists());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/students/{id}")
    class Delete {
        @Test
        @DisplayName("should return 204 when student is deleted")
        void shouldReturn204() throws Exception {
            mockMvc.perform(delete("/api/v1/students/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 404 when student not found")
        void shouldReturn404() throws Exception {
            doThrow(new ResourceNotFoundException("Student", 99L))
                    .when(studentService).delete(99L);

            mockMvc.perform(delete("/api/v1/students/99"))
                    .andExpect(status().isNotFound());
        }
    }
}
