package com.app.job_management.controller;

import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.ApplicationResponse;
import com.app.job_management.service.ApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyApplicationController {

    private final ApplicationService applicationService;

    public CompanyApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/api/company/jobs/{id}/applications")
    public ApiResponse<List<ApplicationResponse>> getJobApplications(
            Authentication authentication,
            @PathVariable Long id) {
        List<ApplicationResponse> response = applicationService.getCompanyJobApplications(authentication.getName(), id);
        return ApiResponse.success("Job applications retrieved successfully", response);
    }

    @PatchMapping("/api/company/applications/{id}/accept")
    public ApiResponse<ApplicationResponse> acceptApplication(
            Authentication authentication,
            @PathVariable Long id) {
        ApplicationResponse response = applicationService.acceptApplication(authentication.getName(), id);
        return ApiResponse.success("Application accepted successfully", response);
    }

    @PatchMapping("/api/company/applications/{id}/reject")
    public ApiResponse<ApplicationResponse> rejectApplication(
            Authentication authentication,
            @PathVariable Long id) {
        ApplicationResponse response = applicationService.rejectApplication(authentication.getName(), id);
        return ApiResponse.success("Application rejected successfully", response);
    }
}