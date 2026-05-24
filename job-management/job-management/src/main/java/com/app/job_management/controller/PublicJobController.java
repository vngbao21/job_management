package com.app.job_management.controller;

import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.JobResponse;
import com.app.job_management.dto.response.PageResponse;
import com.app.job_management.entity.JobType;
import com.app.job_management.service.PublicJobService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class PublicJobController {

    private final PublicJobService publicJobService;

    public PublicJobController(PublicJobService publicJobService) {
        this.publicJobService = publicJobService;
    }

    @GetMapping
    public ApiResponse<PageResponse<JobResponse>> getApprovedJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<JobResponse> response = publicJobService.getApprovedJobs(keyword, location, jobType, page, size);
        return ApiResponse.success("Approved jobs retrieved successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getApprovedJobDetail(@PathVariable Long id) {
        JobResponse response = publicJobService.getApprovedJobDetail(id);
        return ApiResponse.success("Approved job retrieved successfully", response);
    }
}
