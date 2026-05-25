package com.app.job_management.service;

import com.app.job_management.dto.response.UserResponse;
import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.entity.UserStatus;
import com.app.job_management.exception.ApiException;
import com.app.job_management.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse activateUser(Long userId) {
        User user = getUser(userId);
        user.setStatus(UserStatus.ACTIVE);
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse deactivateUser(Long userId) {
        User user = getUser(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Admin users cannot be deactivated");
        }

        user.setStatus(UserStatus.INACTIVE);
        return UserResponse.from(userRepository.save(user));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
