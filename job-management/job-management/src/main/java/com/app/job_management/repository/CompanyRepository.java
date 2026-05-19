package com.app.job_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.job_management.entity.Company;
import com.app.job_management.entity.User;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUser(User user);

    boolean existsByUser(User user);
}
