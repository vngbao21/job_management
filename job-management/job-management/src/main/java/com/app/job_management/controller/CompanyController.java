package com.app.job_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.job_management.service.CompanyService;

import com.app.job_management.dto.response.ApiResponse;
import jakarta.validation.Valid;

import com.app.job_management.dto.request.CompanyProfileRequest;
import com.app.job_management.dto.response.CompanyResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/company/profile")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ApiResponse<CompanyResponse> createProfile(Authentication authentication,
            @Valid @RequestBody CompanyProfileRequest request) {
        CompanyResponse companyResponse = companyService.createProfile(authentication.getName(), request);
        return ApiResponse.success("Company profile created successfully", companyResponse);
    }

    @GetMapping
    public ApiResponse<CompanyResponse> getProfile(Authentication authentication) {
        CompanyResponse companyResponse = companyService.getProfile(authentication.getName());
        return ApiResponse.success("Company profile retrieved successfully", companyResponse);
    }

    @PutMapping
    public ApiResponse<CompanyResponse> updateProfile(Authentication authentication,
            @Valid @RequestBody CompanyProfileRequest request) {
        CompanyResponse companyResponse = companyService.updateProfile(authentication.getName(), request);
        return ApiResponse.success("Company profile updated successfully", companyResponse);
    }

}
