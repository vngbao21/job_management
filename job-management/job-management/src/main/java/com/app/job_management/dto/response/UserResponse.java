package com.app.job_management.dto.response;

import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String phone,
        Role role) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getRole());
    }

}
