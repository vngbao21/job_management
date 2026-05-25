export const apiConfig = {
  baseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  paths: {
    health: import.meta.env.VITE_HEALTH_PATH || '/health',
    authLogin: import.meta.env.VITE_AUTH_LOGIN_PATH || '/auth/login',
    authRegister: import.meta.env.VITE_AUTH_REGISTER_PATH || '/auth/register',
    authMe: import.meta.env.VITE_AUTH_ME_PATH || '/auth/me',
    publicJobs: import.meta.env.VITE_PUBLIC_JOBS_PATH || '/jobs',
    companyProfile: import.meta.env.VITE_COMPANY_PROFILE_PATH || '/company/profile',
    companyJobs: import.meta.env.VITE_COMPANY_JOBS_PATH || '/company/jobs',
    adminPendingJobs: import.meta.env.VITE_ADMIN_PENDING_JOBS_PATH || '/admin/jobs/pending',
    adminApproveJob: import.meta.env.VITE_ADMIN_APPROVE_JOB_PATH || '/admin/jobs/:id/approve',
    adminRejectJob: import.meta.env.VITE_ADMIN_REJECT_JOB_PATH || '/admin/jobs/:id/reject',
    adminUsers: import.meta.env.VITE_ADMIN_USERS_PATH || '/admin/users',
    adminActivateUser: import.meta.env.VITE_ADMIN_ACTIVATE_USER_PATH || '/admin/users/:id/active',
    adminDeactivateUser: import.meta.env.VITE_ADMIN_DEACTIVATE_USER_PATH || '/admin/users/:id/inactive',
    adminDashboard: import.meta.env.VITE_ADMIN_DASHBOARD_PATH || '/admin/dashboard',
    companyDashboard: import.meta.env.VITE_COMPANY_DASHBOARD_PATH || '/company/dashboard',
    uploadCv: import.meta.env.VITE_UPLOAD_CV_PATH || '/files/cv',
    applyJob: import.meta.env.VITE_APPLY_JOB_PATH || '/jobs/:id/apply',
    candidateApplications: import.meta.env.VITE_CANDIDATE_APPLICATIONS_PATH || '/candidate/applications',
    companyJobApplications:
      import.meta.env.VITE_COMPANY_JOB_APPLICATIONS_PATH || '/company/jobs/:id/applications',
    companyAcceptApplication:
      import.meta.env.VITE_COMPANY_ACCEPT_APPLICATION_PATH || '/company/applications/:id/accept',
    companyRejectApplication:
      import.meta.env.VITE_COMPANY_REJECT_APPLICATION_PATH || '/company/applications/:id/reject',
  },
}

export function endpoint(path: string, params: Record<string, string | number> = {}) {
  return Object.entries(params).reduce(
    (resolvedPath, [key, value]) => resolvedPath.replace(`:${key}`, String(value)),
    path,
  )
}
