package com.app.job_management.service;

import com.app.job_management.dto.request.ApplicationRequest;
import com.app.job_management.dto.response.ApplicationResponse;
import com.app.job_management.entity.*;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.CompanyRepository;
import com.app.job_management.repository.JobApplicationRepository;
import com.app.job_management.repository.JobRepository;
import com.app.job_management.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public ApplicationService(
            JobApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public ApplicationResponse applyJob(String email, Long jobId, ApplicationRequest request) {
        User candidate = getCandidateUser(email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        if (job.getStatus() != JobStatus.APPROVED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only approved jobs can be applied");
        }

        if (applicationRepository.existsByJobAndCandidate(job, candidate)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You already applied to this job");
        }

        JobApplication application = new JobApplication(
                job,
                candidate,
                request.cvUrl(),
                request.coverLetter());

        JobApplication savedApplication = applicationRepository.save(application);
        return ApplicationResponse.from(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getCandidateApplications(String email) {
        User candidate = getCandidateUser(email);

        return applicationRepository.findByCandidate(candidate)
                .stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getCompanyJobApplications(String email, Long jobId) {
        Company company = getCompanyProfile(email);

        Job job = jobRepository.findByIdAndCompany(jobId, company)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        return applicationRepository.findByJob(job)
                .stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    public ApplicationResponse acceptApplication(String email, Long applicationId) {
        Company company = getCompanyProfile(email);

        JobApplication application = applicationRepository.findByIdAndJobCompanyId(applicationId, company.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));

        application.setStatus(ApplicationStatus.ACCEPTED);

        JobApplication savedApplication = applicationRepository.save(application);
        return ApplicationResponse.from(savedApplication);
    }

    public ApplicationResponse rejectApplication(String email, Long applicationId) {
        Company company = getCompanyProfile(email);

        JobApplication application = applicationRepository.findByIdAndJobCompanyId(applicationId, company.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));

        application.setStatus(ApplicationStatus.REJECTED);

        JobApplication savedApplication = applicationRepository.save(application);
        return ApplicationResponse.from(savedApplication);
    }

    private User getCandidateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.CANDIDATE) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only candidate users can apply jobs");
        }

        return user;
    }

    private Company getCompanyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.COMPANY) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only company users can manage applications");
        }

        return companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Company profile is required"));
    }
}