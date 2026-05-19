package com.app.job_management.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.job_management.repository.CompanyRepository;
import com.app.job_management.repository.UserRepository;
import com.app.job_management.dto.request.CompanyProfileRequest;
import com.app.job_management.dto.response.CompanyResponse;
import com.app.job_management.entity.Company;
import com.app.job_management.entity.Role;
import com.app.job_management.entity.User;
import com.app.job_management.exception.ApiException;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public CompanyResponse createProfile(String email, CompanyProfileRequest request) {
        User user = getCompanyUser((email));

        if (companyRepository.existsByUser(user)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Company profile already exists for this user");
        }

        Company company = new Company(
                user,
                request.companyName(),
                request.description(),
                request.website(),
                request.address());

        Company savedCompany = companyRepository.save(company);
        return CompanyResponse.from(savedCompany);
    }

    public CompanyResponse getProfile(String email) {
        User user = getCompanyUser(email);

        Company company = companyRepository.findByUser(user)
                .orElseThrow(
                        () -> new ApiException(HttpStatus.NOT_FOUND, "Company profile not found for user: " + email));
        return CompanyResponse.from(company);
    }

    public CompanyResponse updateProfile(String email, CompanyProfileRequest request) {
        User user = getCompanyUser(email);

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Company profile not found"));

        company.setCompanyName(request.companyName());
        company.setDescription(request.description());
        company.setWebsite(request.website());
        company.setAddress(request.address());

        Company savedCompany = companyRepository.save(company);
        return CompanyResponse.from(savedCompany);
    }

    private User getCompanyUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole() != Role.COMPANY) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only Company users can manage company profiles");
        }
        return user;
    }
}
