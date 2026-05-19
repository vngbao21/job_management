package com.app.job_management.dto.request;

import com.app.job_management.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
                @NotBlank @Email String email,

                @NotBlank @Size(min = 6, message = "Password must be at least 6 characters long") String password,

                @NotBlank String fullName,

                String phone,

                @NotNull Role role

) {

}
