package com.app.job_management.service;

import com.app.job_management.dto.request.ApplicationRequest;
import com.app.job_management.entity.Company;
import com.app.job_management.entity.Job;
import com.app.job_management.entity.JobStatus;
import com.app.job_management.entity.JobType;
import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.CompanyRepository;
import com.app.job_management.repository.JobApplicationRepository;
import com.app.job_management.repository.JobRepository;
import com.app.job_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    JobApplicationRepository applicationRepository;

    @Mock
    JobRepository jobRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    ApplicationService applicationService;

    @Test
    void applyJob_shouldAllowOnlyCandidateUsers() {
        User companyUser = user("company@example.com", Role.COMPANY);
        when(userRepository.findByEmail("company@example.com")).thenReturn(Optional.of(companyUser));

        ApplicationRequest request = new ApplicationRequest("/uploads/cv/cv.pdf", "Please review my CV");

        assertThatThrownBy(() -> applicationService.applyJob("company@example.com", 1L, request))
                .isInstanceOf(ApiException.class)
                .satisfies(exception -> {
                    ApiException apiException = (ApiException) exception;
                    assertThat(apiException.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(apiException.getMessage()).isEqualTo("Only candidate users can apply jobs");
                });

        verify(jobRepository, never()).findById(org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    void applyJob_shouldBlockDuplicateApplication() {
        User candidate = user("candidate@example.com", Role.CANDIDATE);
        Job approvedJob = approvedJob();

        when(userRepository.findByEmail("candidate@example.com")).thenReturn(Optional.of(candidate));
        when(jobRepository.findById(1L)).thenReturn(Optional.of(approvedJob));
        when(applicationRepository.existsByJobAndCandidate(approvedJob, candidate)).thenReturn(true);

        ApplicationRequest request = new ApplicationRequest("/uploads/cv/cv.pdf", "Please review my CV");

        assertThatThrownBy(() -> applicationService.applyJob("candidate@example.com", 1L, request))
                .isInstanceOf(ApiException.class)
                .satisfies(exception -> {
                    ApiException apiException = (ApiException) exception;
                    assertThat(apiException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(apiException.getMessage()).isEqualTo("You already applied to this job");
                });

        verify(applicationRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    private User user(String email, Role role) {
        return new User(email, "encoded-password", "Test User", "0900000000", role);
    }

    private Job approvedJob() {
        User companyUser = user("company@example.com", Role.COMPANY);
        Company company = new Company(companyUser, "ABC Tech", "Software company", "https://abc.com", "HCM");
        Job job = new Job(
                company,
                "Java Backend Developer",
                "Build APIs",
                "Java, Spring Boot",
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(2000),
                "Ho Chi Minh City",
                JobType.FULL_TIME);
        job.setStatus(JobStatus.APPROVED);
        return job;
    }
}
