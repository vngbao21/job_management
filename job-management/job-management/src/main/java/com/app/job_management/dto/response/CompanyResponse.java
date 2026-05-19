package com.app.job_management.dto.response;

import java.time.LocalDateTime;

import com.app.job_management.entity.Company;

public record CompanyResponse(
        Long id,
        Long userId,
        String companyName,
        String website,
        String address,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getUser().getId(),
                company.getCompanyName(),
                company.getWebsite(),
                company.getAddress(),
                company.getDescription(),
                company.getCreatedAt(),
                company.getUpdatedAt());
    }

}
