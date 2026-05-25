package com.app.job_management.controller;

import com.app.job_management.dto.response.ApiResponse;
import com.app.job_management.dto.response.FileUploadResponse;
import com.app.job_management.service.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/cv")
    public ApiResponse<FileUploadResponse> uploadCv(@RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.uploadCv(file);
        return ApiResponse.success("CV uploaded successfully", response);
    }
}
