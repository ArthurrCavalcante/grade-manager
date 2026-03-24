package com.arthur.grademanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseDTO {

    public record Request(
        @NotBlank(message = "Course name is required")
        String name,

        @NotBlank(message = "Course code is required")
        String code,

        @NotNull(message = "Workload is required")
        @Min(value = 1, message = "Workload must be at least 1 hour")
        Integer workload
    ) {}

    public record Response(
        Long id,
        String name,
        String code,
        Integer workload
    ) {}
}
