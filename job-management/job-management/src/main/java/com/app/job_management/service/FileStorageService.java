package com.app.job_management.service;

import com.app.job_management.dto.response.FileUploadResponse;
import com.app.job_management.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_CV_EXTENSIONS = Set.of("pdf", "doc", "docx");

    private final Path cvUploadPath;

    public FileStorageService(@Value("${app.upload.cv-dir}") String cvUploadDir) {
        this.cvUploadPath = Path.of(cvUploadDir).toAbsolutePath().normalize();
    }

    public FileUploadResponse uploadCv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "CV file is required");
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_CV_EXTENSIONS.contains(extension)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only PDF, DOC, and DOCX CV files are allowed");
        }

        try {
            Files.createDirectories(cvUploadPath);

            String fileName = UUID.randomUUID() + "." + extension;
            Path destination = cvUploadPath.resolve(fileName).normalize();

            if (!destination.startsWith(cvUploadPath)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid file name");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            return new FileUploadResponse(fileName, "/uploads/cv/" + fileName, file.getSize());
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not upload CV file");
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "CV file must have an extension");
        }

        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
