package com.app.job_management.service;

import com.app.job_management.dto.request.JobRequest;
import com.app.job_management.dto.response.JobResponse;
import com.app.job_management.entity.Company;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.CompanyRepository;
import com.app.job_management.repository.JobRepository;
import com.app.job_management.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public JobService(
            JobRepository jobRepository,
            CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public JobResponse createCompanyJob(String email, JobRequest request) {
        Company company = getCompanyProfile(email);

        Job job = new Job(
                company,
                request.title(),
                request.description(),
                request.requirement(),
                request.salaryMin(),
                request.salaryMax(),
                request.location(),
                request.jobType());

        Job savedJob = jobRepository.save(job);
        return JobResponse.from(savedJob);
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getCompanyJobs(String email) {
        Company company = getCompanyProfile(email);

        return jobRepository.findByCompany(company)
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public JobResponse getCompanyJobDetail(String email, Long jobId) {
        Company company = getCompanyProfile(email);

        Job job = jobRepository.findByIdAndCompany(jobId, company)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        return JobResponse.from(job);
    }

    public JobResponse updateCompanyJob(String email, Long jobId, JobRequest request) {
        Company company = getCompanyProfile(email);

        Job job = jobRepository.findByIdAndCompany(jobId, company)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setRequirement(request.requirement());
        job.setSalaryMin(request.salaryMin());
        job.setSalaryMax(request.salaryMax());
        job.setLocation(request.location());
        job.setJobType(request.jobType());

        Job savedJob = jobRepository.save(job);
        return JobResponse.from(savedJob);
    }

    public void deleteCompanyJob(String email, Long jobId) {
        Company company = getCompanyProfile(email);

        Job job = jobRepository.findByIdAndCompany(jobId, company)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        jobRepository.delete(job);
    }

    private Company getCompanyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.COMPANY) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only company users can manage jobs");
        }

        return companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                        "Company profile is required before creating jobs"));
    }
}
