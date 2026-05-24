import type { Job, JobPage, JobSearchParams } from '../entities/job'

export interface JobRepository {
  getApprovedJobs(params: JobSearchParams): Promise<JobPage>
  getApprovedJobDetail(id: number): Promise<Job>
}
