package com.arthur.grademanager.mapper;

import com.arthur.grademanager.dto.StudentDTO;
import com.arthur.grademanager.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO.Response toResponse(Student student) {
        return new StudentDTO.Response(
                student.getId(),
                student.getName(),
                student.getRegistration(),
                student.getEmail()
        );
    }

    public StudentDTO.Summary toSummary(Student student) {
        return new StudentDTO.Summary(
                student.getId(),
                student.getName(),
                student.getRegistration()
        );
    }
}
