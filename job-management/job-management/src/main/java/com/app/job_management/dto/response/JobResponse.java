package com.app.job_management.dto.response;

import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobStatus;
import com.app.job_management.entity.JobType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        Long companyId,
        String companyName,
        String title,
        String description,
        String requirement,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        String location,
        JobType jobType,
        JobStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static JobResponse from(Job job) {
        return new JobResponse(
                job.getId(),
                job.getCompany().getId(),
                job.getCompany().getCompanyName(),
                job.getTitle(),
                job.getDescription(),
                job.getRequirement(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getLocation(),
                job.getJobType(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getUpdatedAt());
    }
}