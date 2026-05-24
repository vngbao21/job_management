package com.app.job_management.controller;

import com.app.job_management.dto.request.ApplicationRequest;
import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.ApplicationResponse;
import com.app.job_management.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CandidateApplicationController {

    private final ApplicationService applicationService;

    public CandidateApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/api/jobs/{id}/apply")
    public ApiResponse<ApplicationResponse> applyJob(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequest request) {
        ApplicationResponse response = applicationService.applyJob(authentication.getName(), id, request);
        return ApiResponse.success("Job applied successfully", response);
    }

    @GetMapping("/api/candidate/applications")
    public ApiResponse<List<ApplicationResponse>> getMyApplications(Authentication authentication) {
        List<ApplicationResponse> response = applicationService.getCandidateApplications(authentication.getName());
        return ApiResponse.success("Candidate applications retrieved successfully", response);
    }
}