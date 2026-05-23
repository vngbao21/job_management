package com.app.job_management.controller;

import com.app.job_management.dto.request.JobRequest;
import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.JobResponse;
import com.app.job_management.service.JobService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/jobs")
public class CompanyJobController {

    private final JobService jobService;

    public CompanyJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ApiResponse<JobResponse> createJob(
            Authentication authentication,
            @Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.createCompanyJob(authentication.getName(), request);
        return ApiResponse.success("Job created successfully", response);
    }

    @GetMapping
    public ApiResponse<List<JobResponse>> getJobs(Authentication authentication) {
        List<JobResponse> response = jobService.getCompanyJobs(authentication.getName());
        return ApiResponse.success("Company jobs retrieved successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getJobDetail(
            Authentication authentication,
            @PathVariable Long id) {
        JobResponse response = jobService.getCompanyJobDetail(authentication.getName(), id);
        return ApiResponse.success("Company job retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<JobResponse> updateJob(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {
        JobResponse response = jobService.updateCompanyJob(authentication.getName(), id, request);
        return ApiResponse.success("Job updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJob(
            Authentication authentication,
            @PathVariable Long id) {
        jobService.deleteCompanyJob(authentication.getName(), id);
        return ApiResponse.success("Job deleted successfully", null);
    }
}