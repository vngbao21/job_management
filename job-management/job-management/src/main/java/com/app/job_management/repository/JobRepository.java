package com.app.job_management.repository;

import com.app.job_management.entity.Company;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompany(Company company);

    Optional<Job> findByIdAndCompany(Long id, Company company);

    List<Job> findByStatus(JobStatus status);
}