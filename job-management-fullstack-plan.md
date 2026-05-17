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
