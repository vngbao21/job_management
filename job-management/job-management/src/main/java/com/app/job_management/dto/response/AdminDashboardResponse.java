package com.app.job_management.dto.response;

public record AdminDashboardResponse(
        long totalUsers,
        long activeUsers,
        long inactiveUsers,
        long totalCompanies,
        long totalCandidates,
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
