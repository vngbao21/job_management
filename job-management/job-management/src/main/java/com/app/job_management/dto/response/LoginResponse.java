package com.app.job_management.dto.response;

public record LoginResponse(
        String accessToken,
        String tokenType,
        UserResponse user) {
    public static LoginResponse of(String accessToken, UserResponse user) {
        return new LoginResponse(accessToken, "Bearer", user);
    }
}