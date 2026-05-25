package com.app.job_management.service;

import com.app.job_management.dto.response.AdminDashboardResponse;
import com.app.job_management.dto.response.CompanyDashboardResponse;
import com.app.job_management.entity.ApplicationStatus;
import com.app.job_management.entity.Company;
import com.app.job_management.entity.JobStatus;
import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.entity.UserStatus;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.CompanyRepository;
import com.app.job_management.repository.JobApplicationRepository;
import com.app.job_management.repository.JobRepository;
import com.app.job_management.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public DashboardService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public AdminDashboardResponse getAdminDashboard() {
        return new AdminDashboardResponse(
                userRepository.count(),
                userRepository.countByStatus(UserStatus.ACTIVE),
                userRepository.countByStatus(UserStatus.INACTIVE),
                userRepository.countByRole(Role.COMPANY),
                userRepository.countByRole(Role.CANDIDATE),
                jobRepository.count(),
                jobRepository.countByStatus(JobStatus.PENDING),
                jobRepository.countByStatus(JobStatus.APPROVED),
                jobRepository.countByStatus(JobStatus.REJECTED),
                jobRepository.countByStatus(JobStatus.CLOSED),
                jobApplicationRepository.count(),
                jobApplicationRepository.countByStatus(ApplicationStatus.PENDING),
                jobApplicationRepository.countByStatus(ApplicationStatus.ACCEPTED),
                jobApplicationRepository.countByStatus(ApplicationStatus.REJECTED));
    }

    public CompanyDashboardResponse getCompanyDashboard(String email) {
        Company company = getCompanyProfile(email);
        Long companyId = company.getId();

        return new CompanyDashboardResponse(
                companyId,
                company.getCompanyName(),
                jobRepository.countByCompany(company),
                jobRepository.countByCompanyAndStatus(company, JobStatus.PENDING),
                jobRepository.countByCompanyAndStatus(company, JobStatus.APPROVED),
                jobRepository.countByCompanyAndStatus(company, JobStatus.REJECTED),
                jobRepository.countByCompanyAndStatus(company, JobStatus.CLOSED),
                jobApplicationRepository.countByJobCompanyId(companyId),
                jobApplicationRepository.countByJobCompanyIdAndStatus(companyId, ApplicationStatus.PENDING),
                jobApplicationRepository.countByJobCompanyIdAndStatus(companyId, ApplicationStatus.ACCEPTED),
                jobApplicationRepository.countByJobCompanyIdAndStatus(companyId, ApplicationStatus.REJECTED));
    }

    private Company getCompanyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.COMPANY) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only Company users can view company dashboard");
        }

        return companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Company profile not found"));
    }
}
