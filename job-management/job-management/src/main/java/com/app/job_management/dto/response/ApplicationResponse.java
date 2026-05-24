package com.app.job_management.dto.response;

import com.app.job_management.entity.ApplicationStatus;
import com.app.job_management.entity.JobApplication;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long jobId,
        String jobTitle,
        Long companyId,
        String companyName,
        Long candidateId,
        String candidateName,
        String candidateEmail,
        String cvUrl,
        String coverLetter,
        ApplicationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static ApplicationResponse from(JobApplication application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getJob().getCompany().getId(),
                application.getJob().getCompany().getCompanyName(),
                application.getCandidate().getId(),
                application.getCandidate().getFullName(),
                application.getCandidate().getEmail(),
                application.getCvUrl(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getCreatedAt(),
                application.getUpdatedAt());
    }
}