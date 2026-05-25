import type { CompanyProfile, CompanyProfilePayload } from '../entities/company'
import type { CompanyDashboard } from '../entities/dashboard'
import type { Job, JobPayload } from '../entities/job'

export interface CompanyRepository {
  getDashboard(accessToken: string): Promise<CompanyDashboard>
  getProfile(accessToken: string): Promise<CompanyProfile>
  createProfile(accessToken: string, payload: CompanyProfilePayload): Promise<CompanyProfile>
  updateProfile(accessToken: string, payload: CompanyProfilePayload): Promise<CompanyProfile>
  getJobs(accessToken: string): Promise<Job[]>
  createJob(accessToken: string, payload: JobPayload): Promise<Job>
  updateJob(accessToken: string, id: number, payload: JobPayload): Promise<Job>
  deleteJob(accessToken: string, id: number): Promise<void>
}
