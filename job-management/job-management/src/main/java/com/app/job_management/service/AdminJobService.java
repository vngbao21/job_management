package com.app.job_management.service;

import com.app.job_management.dto.response.JobResponse;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobStatus;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.JobRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminJobService {

    private final JobRepository jobRepository;

    public AdminJobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getPendingJobs() {
        return jobRepository.findByStatus(JobStatus.PENDING)
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    public JobResponse approveJob(Long jobId) {
        Job job = getJob(jobId);

        job.setStatus(JobStatus.APPROVED);

        Job savedJob = jobRepository.save(job);
        return JobResponse.from(savedJob);
    }

    public JobResponse rejectJob(Long jobId) {
        Job job = getJob(jobId);

        job.setStatus(JobStatus.REJECTED);

        Job savedJob = jobRepository.save(job);
        return JobResponse.from(savedJob);
    }

    private Job getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));
    }
}