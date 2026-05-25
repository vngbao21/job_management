# Job Management Recruitment Platform

Full-stack recruitment platform with three roles: Admin, Company, and Candidate. The backend is Spring Boot with JWT authentication and MySQL. The current frontend is Vue 3 + TypeScript.

## Features

- JWT register, login, and `GET /api/auth/me`
- Role-based access control for Admin, Company, and Candidate
- Public approved job list, detail, search, filter, and pagination
- Company profile management
- Company job create, update, delete, and salary range validation
- Admin job approval/rejection
- Admin user management with active/inactive status
- Candidate application flow with CV upload
- Company application review with accept/reject actions
- Admin and Company dashboard statistics
- Swagger/OpenAPI and Postman collection

## Tech Stack

Backend:

- Java 21
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL 8
- Bean Validation
- Springdoc OpenAPI
- Docker

Frontend:

- Vue 3
- TypeScript
- Vite
- Native fetch repository layer

## Project Structure

```txt
.
├── Fe-App/                          # Vue frontend
├── job-management/job-management/    # Spring Boot backend
├── postman/                          # Postman collection
├── docker-compose.yml                # MySQL + backend
├── README.md
└── job-management-fullstack-plan.md
```

## Run Backend Locally

Create the database:

```sql
CREATE DATABASE job_management;
```

Configure backend env:

```bash
cd job-management/job-management
copy .env.example .env
```

Example `.env`:

```txt
SERVER_PORT=8080
DB_URL=jdbc:mysql://localhost:3306/job_management
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=job-management-secret-key-must-be-at-least-32-characters
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:5174,http://localhost:5175
CV_UPLOAD_DIR=uploads/cv
APP_SEED_ADMIN_EMAIL=admin@example.com
APP_SEED_ADMIN_PASSWORD=123456
```

Run tests:

```bash
cmd /c mvnw.cmd test
```

Start backend:

```bash
cmd /c mvnw.cmd spring-boot:run
```

Useful URLs:

```txt
Backend:    http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html
Health:     http://localhost:8080/api/health
```

## Run With Docker

From the repository root:

```bash
docker compose up --build
```

Docker services:

```txt
MySQL:   localhost:3307
Backend: http://localhost:8080
```

Stop containers:

```bash
docker compose down
```

Remove Docker volumes when you want a fresh database and fresh uploaded CV files:

```bash
docker compose down -v
```

## Run Frontend

```bash
cd Fe-App
npm install
npm run dev -- --host=127.0.0.1 --port=5173
```

Frontend URL:

```txt
http://localhost:5173
```

Build frontend:

```bash
npm run build
```

## API Summary

Auth:

```txt
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

Public jobs:

```txt
GET /api/jobs?keyword=java&location=Ho Chi Minh&jobType=FULL_TIME&page=0&size=10
GET /api/jobs/{id}
```

Candidate:

```txt
POST /api/files/cv
POST /api/jobs/{id}/apply
GET  /api/candidate/applications
```

Company:

```txt
POST   /api/company/profile
GET    /api/company/profile
PUT    /api/company/profile
GET    /api/company/dashboard
POST   /api/company/jobs
GET    /api/company/jobs
GET    /api/company/jobs/{id}
PUT    /api/company/jobs/{id}
DELETE /api/company/jobs/{id}
GET    /api/company/jobs/{id}/applications
PATCH  /api/company/applications/{id}/accept
PATCH  /api/company/applications/{id}/reject
```

Admin:

```txt
GET   /api/admin/dashboard
GET   /api/admin/jobs/pending
PATCH /api/admin/jobs/{id}/approve
PATCH /api/admin/jobs/{id}/reject
GET   /api/admin/users
PATCH /api/admin/users/{id}/active
PATCH /api/admin/users/{id}/inactive
```

## Postman

Import this file:

```txt
postman/job-management-api.postman_collection.json
```

Suggested test order:

```txt
1. Login Admin
2. Register/Login Company
3. Create Company Profile
4. Create Job
5. Approve Job as Admin
6. Register/Login Candidate
7. Upload CV
8. Apply Job
9. Review Application as Company
10. Check Admin/Company Dashboard
```

## Demo Admin

The backend seeds an admin user on startup if it does not exist:

```txt
admin@example.com / 123456
```

Company and Candidate accounts can be created through `POST /api/auth/register`.

## Tests

Current test coverage includes:

```txt
AuthService: block public admin registration, block inactive login
ApplicationService: only candidates can apply, duplicate applications are blocked
AdminUserService: admin users cannot be deactivated
FileStorageService: valid CV upload and invalid extension rejection
Spring context smoke test
```

Run:

```bash
cd job-management/job-management
cmd /c mvnw.cmd test
```
