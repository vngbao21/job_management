package com.app.job_management.dto.request;

import com.app.job_management.entity.JobType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record JobRequest(
        @NotBlank @Size(max = 180) String title,

        @NotBlank String description,

        String requirement,

        @PositiveOrZero BigDecimal salaryMin,

        @PositiveOrZero BigDecimal salaryMax,

        @NotBlank @Size(max = 150) String location,

        @NotNull JobType jobType) {

    @AssertTrue(message = "salaryMin must be less than or equal to salaryMax")
    public boolean isSalaryRangeValid() {
        return salaryMin == null || salaryMax == null || salaryMin.compareTo(salaryMax) <= 0;
    }
}
