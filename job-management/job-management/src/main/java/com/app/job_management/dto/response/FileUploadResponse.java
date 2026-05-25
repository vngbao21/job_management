package com.app.job_management.dto.response;

public record FileUploadResponse(
        String fileName,
        String url,
        long size) {
}
