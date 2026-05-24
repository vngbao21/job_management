# Job Management Frontend

Vue 3 + TypeScript frontend for the Job Management recruitment platform.

## What This App Does

- Shows approved public jobs with backend search, filter, and pagination.
- Supports login/register with JWT-backed backend APIs.
- Provides role-specific dashboards for Candidate, Company, and Admin users.
- Lets candidates apply to jobs and view application history.
- Lets companies manage profile/job data and accept or reject applications.
- Lets admins approve or reject pending jobs.
- Shows loading and toast feedback for API success/error/info states.

## Stack

- Vue 3
- TypeScript
- Vite
- Native `fetch` API repository layer
- Plain CSS in `src/style.css`

## Setup

```bash
npm install
```

Create `.env` if the backend API URL is different from the default:

```txt
VITE_API_BASE_URL=http://localhost:8080/api
```

## Development

```bash
npm run dev -- --host=127.0.0.1 --port=5173
```

If port `5173` is busy, Vite will choose the next available port.

## Build

```bash
npm run build
```

## Backend API Dependencies

The frontend expects the backend API to expose:

```txt
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me

GET /api/jobs?keyword=&location=&jobType=&page=0&size=10
GET /api/jobs/{id}

POST   /api/company/profile
GET    /api/company/profile
PUT    /api/company/profile
POST   /api/company/jobs
GET    /api/company/jobs
PUT    /api/company/jobs/{id}
DELETE /api/company/jobs/{id}
GET    /api/company/jobs/{id}/applications
PATCH  /api/company/applications/{id}/accept
PATCH  /api/company/applications/{id}/reject

POST /api/jobs/{id}/apply
GET  /api/candidate/applications

GET   /api/admin/jobs/pending
PATCH /api/admin/jobs/{id}/approve
PATCH /api/admin/jobs/{id}/reject
```

## Notes

- Public job list uses backend-side `PageResponse`.
- Company job salary validation is enforced before submit.
- CV upload is currently sent as a CV reference string until a real upload endpoint exists.
- Admin user management in the UI is still local/demo-level until backend APIs are added.
