package com.app.job_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.job_management.dto.request.RegisterRequest;
import com.app.job_management.dto.request.LoginRequest;
import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.UserResponse;
import com.app.job_management.service.AuthService;
import com.app.job_management.dto.response.LoginResponse;
import com.app.job_management.config.SwaggerConfig;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @GetMapping("/me")
    public ApiResponse<UserResponse> me(Authentication authentication) {
        UserResponse userResponse = authService.me(authentication.getName());
        return ApiResponse.success("Get current user successfully", userResponse);
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.register(request);
        return ApiResponse.success("User registered successfully", userResponse);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ApiResponse.success("Login successful", loginResponse);
    }

}
