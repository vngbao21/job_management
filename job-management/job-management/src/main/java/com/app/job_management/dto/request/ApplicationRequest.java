package com.app.job_management.dto.request;

import jakarta.validation.constraints.Size;

public record ApplicationRequest(
        @Size(max = 500) String cvUrl,
        String coverLetter) {
}