package com.app.job_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyProfileRequest(
                @NotBlank String companyName,
                @Size(max = 500) String description,
                @Size(max = 255) String website,
                @Size(max = 255) String address) {

}
