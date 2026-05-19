# Job Management / Recruitment App - Fullstack Plan

## 1. Mục tiêu dự án

Xây dựng hệ thống tuyển dụng việc làm gồm 3 role chính:

- Admin
- Company
- Candidate

Dự án dùng để luyện Java Backend và làm portfolio xin việc Backend / Fullstack Developer.

---

## 2. Tech Stack

### Backend

```txt
Java 21
Spring Boot 3.x
Spring Security
JWT Authentication
Spring Data JPA / Hibernate
MySQL
Validation
Lombok
Swagger / OpenAPI
Docker
```

### Frontend

```txt
ReactJS
TypeScript
Vite
React Router
Axios
TanStack Query
React Hook Form
Zod
TailwindCSS hoặc MUI
```

### Tools

```txt
VS Code
Postman
MySQL Workbench / DBeaver
Git / GitHub
Docker Desktop
```

---

## 3. Chức năng chính

### Auth

```txt
Đăng ký tài khoản
Đăng nhập
JWT access token
Phân quyền theo role
Logout phía frontend
```

### Role

```txt
ADMIN
COMPANY
CANDIDATE
```

### Candidate

```txt
Xem danh sách job
Search job
Filter job
Xem chi tiết job
Apply job
Upload CV
Xem lịch sử apply
Quản lý profile cá nhân
```

### Company

```txt
Tạo job
Sửa job
Xóa job
Xem danh sách job đã đăng
Xem candidate đã apply
Approve / reject application
```

### Admin

```txt
Duyệt job
Reject job
Quản lý user
Quản lý company
Xem dashboard tổng quan
```

---

## 4. Cấu trúc thư mục tổng thể

```txt
job-management-app/
  backend/
    src/
    pom.xml
    Dockerfile
    application.yml

  frontend/
    src/
    package.json
    vite.config.ts
    Dockerfile

  docker-compose.yml
  README.md
```

---

# BACKEND PLAN

## 5. Tạo Backend Project

Tạo project bằng Spring Initializr:

```txt
Project: Maven
Language: Java
Spring Boot: 3.x
Java: 21
Packaging: Jar
```

Dependencies:

```txt
Spring Web
Spring Security
Spring Data JPA
MySQL Driver
Validation
Lombok
Spring Boot DevTools
```

---

## 6. Backend Folder Structure

```txt
src/main/java/com/example/jobapp/
  JobAppApplication.java

  config/
    SecurityConfig.java
    CorsConfig.java
    SwaggerConfig.java

  controller/
    AuthController.java
    JobController.java
    CompanyJobController.java
    ApplicationController.java
    AdminController.java

  dto/
    request/
    response/

  entity/
    User.java
    Company.java
    Job.java
    JobApplication.java
    Role.java

  repository/
    UserRepository.java
    CompanyRepository.java
    JobRepository.java
    JobApplicationRepository.java

  service/
    AuthService.java
    JobService.java
    ApplicationService.java
    AdminService.java

  security/
    JwtService.java
    JwtAuthenticationFilter.java
    CustomUserDetailsService.java

  exception/
    GlobalExceptionHandler.java
    ApiException.java
```

---

## 7. Database Setup

```sql
CREATE DATABASE job_management;
```

File `application.yml`:

```yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/job_management
    username: root
    password: your_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000
```

---

## 8. Entity chính

### User

```txt
id
email
password
fullName
phone
role
status
createdAt
updatedAt
```

### Company

```txt
id
userId
companyName
description
website
address
createdAt
updatedAt
```

### Job

```txt
id
companyId
title
description
requirement
salaryMin
salaryMax
location
jobType
status
createdAt
updatedAt
```

Status:

```txt
PENDING
APPROVED
REJECTED
CLOSED
```

### JobApplication

```txt
id
jobId
candidateId
cvUrl
coverLetter
status
createdAt
updatedAt
```

Status:

```txt
PENDING
ACCEPTED
REJECTED
```

---

# DATABASE DESIGN V1

## Scope

Database v1 supports the first working recruitment flow:

```txt
Register/Login
JWT auth
Company profile
Company create job
Admin approve/reject job
Candidate view approved jobs
Candidate apply job
Company view job applications
Company accept/reject application
```

## ERD Text

```txt
users 1 ----- 0..1 companies
companies 1 ----- n jobs
users(candidate) 1 ----- n job_applications
jobs 1 ----- n job_applications
```

Meaning:

```txt
One COMPANY user can own one company profile.
One company can post many jobs.
One CANDIDATE user can apply to many jobs.
One job can receive many applications.
One candidate can apply to the same job only once.
```

## Tables

### users

Purpose:

```txt
Store login identity and role for Admin, Company, Candidate.
```

Columns:

```txt
id BIGINT PK AUTO_INCREMENT
email VARCHAR(150) NOT NULL UNIQUE
password VARCHAR(255) NOT NULL
full_name VARCHAR(120) NOT NULL
phone VARCHAR(30)
role VARCHAR(30) NOT NULL
status VARCHAR(30) NOT NULL
created_at DATETIME NOT NULL
updated_at DATETIME NOT NULL
```

Enums:

```txt
role: ADMIN, COMPANY, CANDIDATE
status: ACTIVE, INACTIVE
```

Indexes/constraints:

```txt
UNIQUE(email)
INDEX(role)
INDEX(status)
```

### companies

Purpose:

```txt
Store company profile data owned by a COMPANY user.
```

Columns:

```txt
id BIGINT PK AUTO_INCREMENT
user_id BIGINT NOT NULL
company_name VARCHAR(180) NOT NULL
description TEXT
website VARCHAR(255)
address VARCHAR(255)
created_at DATETIME NOT NULL
updated_at DATETIME NOT NULL
```

Foreign keys:

```txt
user_id -> users.id
```

Indexes/constraints:

```txt
UNIQUE(user_id)
INDEX(company_name)
```

Business rule:

```txt
Only users with role COMPANY should have a company profile.
Application code enforces this rule.
```

### jobs

Purpose:

```txt
Store jobs posted by companies.
Admin approval decides whether a job is visible publicly.
```

Columns:

```txt
id BIGINT PK AUTO_INCREMENT
company_id BIGINT NOT NULL
title VARCHAR(180) NOT NULL
description TEXT NOT NULL
requirement TEXT
salary_min DECIMAL(12,2)
salary_max DECIMAL(12,2)
location VARCHAR(150) NOT NULL
job_type VARCHAR(30) NOT NULL
status VARCHAR(30) NOT NULL
created_at DATETIME NOT NULL
updated_at DATETIME NOT NULL
```

Foreign keys:

```txt
company_id -> companies.id
```

Enums:

```txt
job_type: FULL_TIME, PART_TIME, INTERNSHIP, REMOTE, CONTRACT
status: PENDING, APPROVED, REJECTED, CLOSED
```

Indexes/constraints:

```txt
INDEX(company_id)
INDEX(status)
INDEX(location)
INDEX(job_type)
INDEX(title)
```

Business rules:

```txt
New company jobs start as PENDING.
Only APPROVED jobs are visible in public job list/detail APIs.
CLOSED jobs are no longer open for application.
```

### job_applications

Purpose:

```txt
Store candidate applications to jobs.
```

Columns:

```txt
id BIGINT PK AUTO_INCREMENT
job_id BIGINT NOT NULL
candidate_id BIGINT NOT NULL
cv_url VARCHAR(500)
cover_letter TEXT
status VARCHAR(30) NOT NULL
created_at DATETIME NOT NULL
updated_at DATETIME NOT NULL
```

Foreign keys:

```txt
job_id -> jobs.id
candidate_id -> users.id
```

Enums:

```txt
status: PENDING, ACCEPTED, REJECTED
```

Indexes/constraints:

```txt
UNIQUE(job_id, candidate_id)
INDEX(job_id)
INDEX(candidate_id)
INDEX(status)
```

Business rules:

```txt
Only users with role CANDIDATE can apply.
Candidate can apply to the same job only once.
New applications start as PENDING.
Company can ACCEPT or REJECT applications for its own jobs.
```

## Entity Implementation Order

```txt
1. User - done
2. Company
3. JobStatus, JobType, Job
4. ApplicationStatus, JobApplication
5. Repository layer for Company, Job, JobApplication
6. Company profile API
7. Company job CRUD API
8. Public approved job API
9. Candidate apply API
10. Admin approve/reject job API
```

## Notes

```txt
Use application-level validation for role-specific rules.
Use database constraints for uniqueness and foreign-key integrity.
Use DTOs to avoid exposing entity internals directly.
Do not expose password in any response.
```

---

## 9. Backend API List

### Auth API

```txt
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

### Public Job API

```txt
GET /api/jobs
GET /api/jobs/{id}
```

Example:

```txt
GET /api/jobs?keyword=java&location=hcm&page=0&size=10
```

### Company API

```txt
POST   /api/company/jobs
GET    /api/company/jobs
GET    /api/company/jobs/{id}
PUT    /api/company/jobs/{id}
DELETE /api/company/jobs/{id}
GET    /api/company/jobs/{id}/applications
PATCH  /api/company/applications/{id}/accept
PATCH  /api/company/applications/{id}/reject
```

### Candidate API

```txt
POST /api/jobs/{id}/apply
GET  /api/candidate/applications
GET  /api/candidate/applications/{id}
```

### Admin API

```txt
GET   /api/admin/jobs/pending
PATCH /api/admin/jobs/{id}/approve
PATCH /api/admin/jobs/{id}/reject
GET   /api/admin/users
PATCH /api/admin/users/{id}/active
PATCH /api/admin/users/{id}/inactive
```

---

## 10. Backend Phase

### Phase BE 1: Setup project

```txt
Create Spring Boot project
Connect MySQL
Create base response format
Create GlobalExceptionHandler
Setup Swagger
```

### Phase BE 2: Auth + JWT

```txt
Register
Login
Password encode
Generate JWT
JWT filter
Role-based authorization
```

### Phase BE 3: Job CRUD

```txt
Company create job
Company edit job
Company delete job
Public job list
Public job detail
Search/filter/pagination
```

### Phase BE 4: Apply Job + Upload CV

```txt
Candidate apply job
Upload CV file
Save CV URL/path
Candidate view application history
Company view candidates
```

### Phase BE 5: Admin approval

```txt
Admin approve job
Admin reject job
Only approved jobs visible publicly
Admin manage users
```

### Phase BE 6: Docker + README

```txt
Dockerfile backend
Docker compose with MySQL
Seed sample data
Write API document
```

---

# FRONTEND PLAN

## 11. Tạo Frontend Project

```bash
npm create vite@latest frontend -- --template react-ts
cd frontend
npm install
```

Install packages:

```bash
npm install axios react-router-dom @tanstack/react-query react-hook-form zod @hookform/resolvers
```

Nếu dùng TailwindCSS:

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

Nếu dùng MUI:

```bash
npm install @mui/material @emotion/react @emotion/styled @mui/icons-material
```

---

## 12. Frontend Folder Structure

```txt
src/
  main.tsx
  App.tsx

  api/
    axiosClient.ts
    authApi.ts
    jobApi.ts
    applicationApi.ts
    adminApi.ts

  components/
    common/
    forms/
    layout/

  pages/
    auth/
      LoginPage.tsx
      RegisterPage.tsx

    candidate/
      JobListPage.tsx
      JobDetailPage.tsx
      MyApplicationsPage.tsx
      CandidateProfilePage.tsx

    company/
      CompanyDashboardPage.tsx
      CompanyJobListPage.tsx
      CreateJobPage.tsx
      EditJobPage.tsx
      JobApplicationsPage.tsx

    admin/
      AdminDashboardPage.tsx
      PendingJobsPage.tsx
      UserManagementPage.tsx

  routes/
    AppRoutes.tsx
    ProtectedRoute.tsx
    RoleRoute.tsx

  hooks/
    useAuth.ts

  stores/
    authStore.ts

  types/
    auth.ts
    job.ts
    application.ts
    user.ts

  utils/
    token.ts
    constants.ts
```

---

## 13. Frontend Phase

### Phase FE 1: Setup UI base

```txt
Setup Vite React TypeScript
Setup Router
Setup Axios client
Setup layout
Setup login/register page UI
```

### Phase FE 2: Auth flow

```txt
Call login API
Save token
Attach token to Axios
ProtectedRoute
RoleRoute
Logout
```

### Phase FE 3: Candidate job flow

```txt
Job list
Job detail
Search/filter/pagination
Apply job
Upload CV
My applications
```

### Phase FE 4: Company dashboard

```txt
Company job list
Create job
Edit job
Delete job
View applications
Accept/reject candidate
```

### Phase FE 5: Admin dashboard

```txt
Pending jobs
Approve/reject jobs
User management
Dashboard stats
```

### Phase FE 6: Polish UI

```txt
Loading state
Error state
Empty state
Toast notification
Responsive layout
Clean README screenshots
```

---

# FULLSTACK EXECUTION ORDER

```txt
1. Setup backend Spring Boot
2. Connect MySQL
3. Create User entity
4. Create Register/Login API
5. Setup JWT security
6. Test Auth API bằng Postman
7. Setup frontend React
8. Build Login/Register UI
9. Connect FE login/register với BE
10. Build Job entity + Job API
11. Build Job list/detail UI
12. Build Company create/edit job
13. Build Candidate apply job
14. Build Admin approve/reject job
15. Add upload CV
16. Add Swagger
17. Add Docker
18. Write README
19. Push GitHub
20. Deploy demo
```

---

## Git Branch Suggestion

```txt
main
 └── develop
      ├── feature/be-auth
      ├── feature/be-job-crud
      ├── feature/be-application
      ├── feature/fe-auth
      ├── feature/fe-job-list
      ├── feature/fe-company-dashboard
      └── feature/fe-admin-dashboard
```

---

## README cần có

```txt
Project introduction
Tech stack
Features
Database design
API document link
How to run backend
How to run frontend
Docker setup
Screenshots
Demo account
```

Demo account:

```txt
Admin:
admin@example.com / 123456

Company:
company@example.com / 123456

Candidate:
candidate@example.com / 123456
```

---

## Portfolio Highlight

```txt
Built a full-stack recruitment platform using Java Spring Boot and React TypeScript.
Implemented JWT authentication, role-based authorization, job approval workflow, CV upload, search/filter/pagination, and Dockerized deployment.
```

---

## Version 1 Scope

Nếu muốn hoàn thành nhanh, version 1 chỉ cần:

```txt
Login/Register
JWT Role
Company create job
Admin approve job
Candidate view job
Candidate apply job
Company view applications
```

Sau khi version 1 chạy ổn mới thêm:

```txt
Email notification
Advanced search
Dashboard chart
CV management
Deploy production
```

---

# PROJECT PROGRESS

Last updated: 2026-05-18

## Backend Progress

### BE 1 - Setup project: Done

Completed:

```txt
Spring Boot backend skeleton
Maven dependencies for WebMVC, Security, JPA, MySQL, Validation, Lombok, Swagger
application.yml base config
.env and .env.example for local config
.gitignore updated to ignore .env secrets
Base API response format
Global exception handler
CORS config
Temporary health endpoint: GET /api/health
Swagger/OpenAPI setup
```

Files added/updated:

```txt
pom.xml
src/main/resources/application.yml
.env
.env.example
src/main/java/com/app/job_management/dto/response/ApiResponse.java
src/main/java/com/app/job_management/exception/ApiException.java
src/main/java/com/app/job_management/exception/GlobalExceptionHandler.java
src/main/java/com/app/job_management/config/CorsConfig.java
src/main/java/com/app/job_management/config/SecurityConfig.java
src/main/java/com/app/job_management/config/SwaggerConfig.java
src/main/java/com/app/job_management/controller/HealthController.java
```

### BE 2 - Auth base: In progress

Completed:

```txt
User entity
Role enum
UserStatus enum
UserRepository
RegisterRequest
LoginRequest
UserResponse
LoginResponse
AuthService register
AuthService login
AuthController register endpoint
AuthController login endpoint
PasswordEncoder with BCrypt
JWT dependency
JwtService generate access token
JwtService extract email from token
CustomUserDetailsService
JwtAuthenticationFilter
SecurityConfig stateless JWT filter
Protected endpoint: GET /api/auth/me
Swagger Bearer JWT security scheme
```

Current Auth APIs:

```txt
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

Files added/updated:

```txt
src/main/java/com/app/job_management/entity/User.java
src/main/java/com/app/job_management/entity/Role.java
src/main/java/com/app/job_management/entity/UserStatus.java
src/main/java/com/app/job_management/repository/UserRepository.java
src/main/java/com/app/job_management/dto/request/RegisterRequest.java
src/main/java/com/app/job_management/dto/request/LoginRequest.java
src/main/java/com/app/job_management/dto/response/UserResponse.java
src/main/java/com/app/job_management/dto/response/LoginResponse.java
src/main/java/com/app/job_management/service/AuthService.java
src/main/java/com/app/job_management/controller/AuthController.java
src/main/java/com/app/job_management/security/JwtService.java
src/main/java/com/app/job_management/security/CustomUserDetailsService.java
src/main/java/com/app/job_management/security/JwtAuthenticationFilter.java
```

Validation status:

```txt
Backend compile: PASS
Register/login code review: PASS
JWT filter compile: PASS
Swagger JWT config compile: PASS
Manual Swagger/Postman test: pending
```

Known notes:

```txt
MySQL/DBeaver connection still needs local confirmation.
Swagger /api/auth/me returns 403 if Authorization header is missing.
Swagger Authorize popup should be used with accessToken from login.
JwtAuthenticationFilter currently parses token; next step should improve invalid/expired token handling.
Test config currently excludes JPA for context test, so full mvnw test may need adjustment after auth wiring.
```

## Next Backend Steps

```txt
1. Manually test POST /api/auth/register with MySQL/DBeaver.
2. Manually test POST /api/auth/login and copy accessToken.
3. Use Swagger Authorize to call GET /api/auth/me.
4. Improve JWT invalid/expired token error handling.
5. Add role-based authorization.
6. Start Job entity and public job APIs.
```
