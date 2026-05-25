package com.app.job_management.dto.response;

public record CompanyDashboardResponse(
        Long companyId,
        String companyName,
        long totalJobs,
        long pendingJobs,
        long approvedJobs,
        long rejectedJobs,
        long closedJobs,
        long totalApplications,
        long pendingApplications,
        long acceptedApplications,
        long rejectedApplications) {
}
