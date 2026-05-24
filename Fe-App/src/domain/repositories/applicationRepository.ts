import type { ApplicationPayload, ApplicationStatus, JobApplication } from '../entities/application'

export interface ApplicationRepository {
  applyJob(accessToken: string, jobId: number, payload: ApplicationPayload): Promise<JobApplication>
  getCandidateApplications(accessToken: string): Promise<JobApplication[]>
  getCompanyJobApplications(accessToken: string, jobId: number): Promise<JobApplication[]>
  reviewApplication(
    accessToken: string,
    id: number,
    status: Exclude<ApplicationStatus, 'PENDING'>,
  ): Promise<JobApplication>
}
