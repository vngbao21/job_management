package com.app.job_management.controller;

import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.JobResponse;
import com.app.job_management.service.AdminJobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/jobs")
public class AdminJobController {

    private final AdminJobService adminJobService;

    public AdminJobController(AdminJobService adminJobService) {
        this.adminJobService = adminJobService;
    }

    @GetMapping("/pending")
    public ApiResponse<List<JobResponse>> getPendingJobs() {
        List<JobResponse> response = adminJobService.getPendingJobs();
        return ApiResponse.success("Pending jobs retrieved successfully", response);
    }

    @PatchMapping("/{id}/approve")
    public ApiResponse<JobResponse> approveJob(@PathVariable Long id) {
        JobResponse response = adminJobService.approveJob(id);
        return ApiResponse.success("Job approved successfully", response);
    }

    @PatchMapping("/{id}/reject")
    public ApiResponse<JobResponse> rejectJob(@PathVariable Long id) {
        JobResponse response = adminJobService.rejectJob(id);
        return ApiResponse.success("Job rejected successfully", response);
    }
}