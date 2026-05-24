import type { Job } from '../entities/job'

export interface AdminRepository {
  getPendingJobs(accessToken: string): Promise<Job[]>
  approveJob(accessToken: string, id: number): Promise<Job>
  rejectJob(accessToken: string, id: number): Promise<Job>
}
