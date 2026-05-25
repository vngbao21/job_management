package com.app.job_management.controller;

import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.UserResponse;
import com.app.job_management.service.AdminUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        List<UserResponse> response = adminUserService.getUsers();
        return ApiResponse.success("Users retrieved successfully", response);
    }

    @PatchMapping("/{id}/active")
    public ApiResponse<UserResponse> activateUser(@PathVariable Long id) {
        UserResponse response = adminUserService.activateUser(id);
        return ApiResponse.success("User activated successfully", response);
    }

    @PatchMapping("/{id}/inactive")
    public ApiResponse<UserResponse> deactivateUser(@PathVariable Long id) {
        UserResponse response = adminUserService.deactivateUser(id);
        return ApiResponse.success("User deactivated successfully", response);
    }
}
