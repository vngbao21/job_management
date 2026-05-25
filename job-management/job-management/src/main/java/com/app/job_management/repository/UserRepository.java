package com.app.job_management.repository;

import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    long countByStatus(UserStatus status);
}
