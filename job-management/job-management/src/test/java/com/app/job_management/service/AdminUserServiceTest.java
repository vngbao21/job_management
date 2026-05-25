package com.app.job_management.service;

import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminUserService adminUserService;

    @Test
    void deactivateUser_shouldBlockAdminUsers() {
        User admin = new User(
                "admin@example.com",
                "encoded-password",
                "Admin User",
                "0900000001",
                Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> adminUserService.deactivateUser(1L))
                .isInstanceOf(ApiException.class)
                .satisfies(exception -> {
                    ApiException apiException = (ApiException) exception;
                    assertThat(apiException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(apiException.getMessage()).isEqualTo("Admin users cannot be deactivated");
                });

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
    }
}
