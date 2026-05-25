export interface AdminDashboard {
  totalUsers: number
  activeUsers: number
  inactiveUsers: number
  totalCompanies: number
  totalCandidates: number
  totalJobs: number
  pendingJobs: number
  approvedJobs: number
  rejectedJobs: number
  closedJobs: number
  totalApplications: number
  pendingApplications: number
  acceptedApplications: number
  rejectedApplications: number
}

export interface CompanyDashboard {
  companyId: number
  companyName: string
  totalJobs: number
  pendingJobs: number
  approvedJobs: number
  rejectedJobs: number
  closedJobs: number
  totalApplications: number
  pendingApplications: number
  acceptedApplications: number
  rejectedApplications: number
}
