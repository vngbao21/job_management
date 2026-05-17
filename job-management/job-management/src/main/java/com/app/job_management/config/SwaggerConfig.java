package com.app.job_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI jobManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Management API")
                        .description("Backend API for recruitment/job management application")
                        .version("v1"));
    }
}
