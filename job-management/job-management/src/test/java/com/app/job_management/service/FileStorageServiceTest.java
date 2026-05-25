package com.app.job_management.service;

import com.app.job_management.dto.response.FileUploadResponse;
import com.app.job_management.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void uploadCv_shouldStoreAllowedCvFile() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                "test cv".getBytes());

        FileUploadResponse response = service.uploadCv(file);

        assertThat(response.fileName()).endsWith(".pdf");
        assertThat(response.url()).isEqualTo("/uploads/cv/" + response.fileName());
        assertThat(response.size()).isEqualTo(file.getSize());
        assertThat(Files.exists(tempDir.resolve(response.fileName()))).isTrue();
    }

    @Test
    void uploadCv_shouldRejectUnsupportedFileExtension() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.exe",
                "application/octet-stream",
                "bad file".getBytes());

        assertThatThrownBy(() -> service.uploadCv(file))
                .isInstanceOf(ApiException.class)
                .satisfies(exception -> {
                    ApiException apiException = (ApiException) exception;
                    assertThat(apiException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(apiException.getMessage()).isEqualTo("Only PDF, DOC, and DOCX CV files are allowed");
                });
    }
}
