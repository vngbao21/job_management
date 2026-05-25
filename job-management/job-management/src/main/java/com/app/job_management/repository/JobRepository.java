package com.app.job_management.repository;

import com.app.job_management.entity.Company;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobStatus;
import com.app.job_management.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompany(Company company);

    Optional<Job> findByIdAndCompany(Long id, Company company);

    List<Job> findByStatus(JobStatus status);

    long countByStatus(JobStatus status);

    long countByCompany(Company company);

    long countByCompanyAndStatus(Company company, JobStatus status);

    Optional<Job> findByIdAndStatus(Long id, JobStatus status);

    @Query("""
            select j from Job j
            where j.status = :status
              and (:keyword is null
                   or lower(j.title) like lower(concat('%', :keyword, '%'))
                   or lower(j.description) like lower(concat('%', :keyword, '%'))
                   or lower(j.requirement) like lower(concat('%', :keyword, '%')))
              and (:location is null
                   or lower(j.location) like lower(concat('%', :location, '%')))
              and (:jobType is null or j.jobType = :jobType)
            """)
    Page<Job> searchByStatus(
            @Param("status") JobStatus status,
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") JobType jobType,
            Pageable pageable);
}
