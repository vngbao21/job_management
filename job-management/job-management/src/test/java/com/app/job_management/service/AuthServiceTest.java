package com.app.job_management.service;

import com.app.job_management.dto.request.LoginRequest;
import com.app.job_management.dto.request.RegisterRequest;
import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.entity.UserStatus;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.UserRepository;
import com.app.job_management.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @Test
    void register_shouldBlockPublicAdminRegistration() {
        RegisterRequest request = new RegisterRequest(
                "admin@example.com",
                "123456",
                "Admin User",
                "0900000001",
                Role.ADMIN);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ApiException.class)
                .satisfies(exception -> {
                    ApiException apiException = (ApiException) exception;
                    assertThat(apiException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(apiException.getMessage()).isEqualTo("Admin registration is not allowed");
                });

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    void login_shouldBlockInactiveUserBeforeCheckingPassword() {
        User inactiveUser = new User(
                "candidate@example.com",
                "encoded-password",
                "Candidate User",
                "0900000003",
                Role.CANDIDATE);
        inactiveUser.setStatus(UserStatus.INACTIVE);

        when(userRepository.findByEmail("candidate@example.com")).thenReturn(Optional.of(inactiveUser));

        LoginRequest request = new LoginRequest("candidate@example.com", "123456");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ApiException.class)
                .satisfies(exception -> {
                    ApiException apiException = (ApiException) exception;
                    assertThat(apiException.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(apiException.getMessage()).isEqualTo("User account is inactive");
                });

        verify(passwordEncoder, never()).matches(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString());
    }
}
