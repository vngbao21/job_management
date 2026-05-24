package com.app.job_management.repository;

import com.app.job_management.entity.ApplicationStatus;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobApplication;
import com.app.job_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobAndCandidate(Job job, User candidate);

    List<JobApplication> findByCandidate(User candidate);

    List<JobApplication> findByJob(Job job);

    Optional<JobApplication> findByIdAndJobCompanyId(Long id, Long companyId);

    List<JobApplication> findByStatus(ApplicationStatus status);
}