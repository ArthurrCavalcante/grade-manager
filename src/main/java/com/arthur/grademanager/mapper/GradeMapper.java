package com.arthur.grademanager.mapper;

import com.arthur.grademanager.dto.GradeDTO;
import com.arthur.grademanager.dto.StudentDTO;
import com.arthur.grademanager.model.Grade;
import com.arthur.grademanager.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GradeMapper {

    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    public GradeDTO.Response toResponse(Grade grade) {
        return new GradeDTO.Response(
                grade.getId(),
                studentMapper.toSummary(grade.getStudent()),
                courseMapper.toResponse(grade.getCourse()),
                grade.getValue(),
                grade.getDate(),
                grade.isApproved()
        );
    }

    public StudentDTO.Summary toStudentSummary(Student student) {
        return studentMapper.toSummary(student);
    }
}
