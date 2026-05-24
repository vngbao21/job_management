package com.app.job_management.service;

import com.app.job_management.dto.response.JobResponse;
import com.app.job_management.dto.response.PageResponse;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobStatus;
import com.app.job_management.entity.JobType;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.JobRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PublicJobService {

    private final JobRepository jobRepository;

    public PublicJobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public PageResponse<JobResponse> getApprovedJobs(
            String keyword,
            String location,
            JobType jobType,
            int page,
            int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        PageRequest pageRequest = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return PageResponse.from(
                jobRepository.searchByStatus(
                        JobStatus.APPROVED,
                        normalize(keyword),
                        normalize(location),
                        jobType,
                        pageRequest),
                JobResponse::from);
    }

    public JobResponse getApprovedJobDetail(Long jobId) {
        Job job = jobRepository.findByIdAndStatus(jobId, JobStatus.APPROVED)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        return JobResponse.from(job);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
