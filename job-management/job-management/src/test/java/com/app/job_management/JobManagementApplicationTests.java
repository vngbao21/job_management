package com.app.job_management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.app.job_management.repository.CompanyRepository;
import com.app.job_management.repository.JobApplicationRepository;
import com.app.job_management.repository.JobRepository;
import com.app.job_management.repository.UserRepository;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
				"org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
})
class JobManagementApplicationTests {

	@MockitoBean
	UserRepository userRepository;

	@MockitoBean
	CompanyRepository companyRepository;

	@MockitoBean
	JobRepository jobRepository;

	@MockitoBean
	JobApplicationRepository jobApplicationRepository;

	@Test
	void contextLoads() {
	}

}
