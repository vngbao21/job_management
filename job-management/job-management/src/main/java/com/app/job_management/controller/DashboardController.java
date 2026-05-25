package com.app.job_management.controller;

import com.app.job_management.dto.response.AdminDashboardResponse;
import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.CompanyDashboardResponse;
import com.app.job_management.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/dashboard")
    public ApiResponse<AdminDashboardResponse> getAdminDashboard() {
        AdminDashboardResponse response = dashboardService.getAdminDashboard();
        return ApiResponse.success("Admin dashboard loaded successfully", response);
    }

    @GetMapping("/company/dashboard")
    public ApiResponse<CompanyDashboardResponse> getCompanyDashboard(Authentication authentication) {
        CompanyDashboardResponse response = dashboardService.getCompanyDashboard(authentication.getName());
        return ApiResponse.success("Company dashboard loaded successfully", response);
    }
}
