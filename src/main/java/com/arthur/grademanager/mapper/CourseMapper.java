package com.arthur.grademanager.mapper;

import com.arthur.grademanager.dto.CourseDTO;
import com.arthur.grademanager.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO.Response toResponse(Course course) {
        return new CourseDTO.Response(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getWorkload()
        );
    }
}
