package com.arthur.grademanager.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class GradeDTO {

    public record Request(
        @NotNull(message = "Student ID is required")
        Long studentId,

        @NotNull(message = "Course ID is required")
        Long courseId,

        @NotNull(message = "Grade value is required")
        @DecimalMin(value = "0.0", message = "Grade cannot be less than 0")
        @DecimalMax(value = "10.0", message = "Grade cannot exceed 10")
        Double value,

        @NotNull(message = "Date is required")
        LocalDate date
    ) {}

    public record Response(
        Long id,
        StudentDTO.Summary student,
        CourseDTO.Response course,
        Double value,
        LocalDate date,
        boolean approved
    ) {}

    public record ReportItem(
        String courseName,
        String courseCode,
        Double grade,
        boolean approved
    ) {}

    public record StudentReport(
        StudentDTO.Summary student,
        List<ReportItem> grades,
        Double average,
        long approvedCount,
        long failedCount
    ) {}
}
