package com.app.job_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Path cvUploadPath;

    public WebMvcConfig(@Value("${app.upload.cv-dir}") String cvUploadDir) {
        this.cvUploadPath = Path.of(cvUploadDir).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/cv/**")
                .addResourceLocations(cvUploadPath.toUri().toString() + "/");
    }
}
