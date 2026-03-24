package com.arthur.grademanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentDTO {

    public record Request(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Registration number is required")
        String registration,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
    ) {}

    public record Response(
        Long id,
        String name,
        String registration,
        String email
    ) {}

    public record Summary(
        Long id,
        String name,
        String registration
    ) {}
}
