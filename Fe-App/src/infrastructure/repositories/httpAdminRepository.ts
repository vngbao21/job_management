import type { AdminRepository } from '../../domain/repositories/adminRepository'
import { apiConfig, endpoint } from '../config/apiConfig'
import { requestApi } from '../http/apiClient'
import { mapJobResponse, type JobResponse } from './jobMapper'

export function createHttpAdminRepository(): AdminRepository {
  return {
    async getPendingJobs(accessToken: string) {
      const jobs = await requestApi<JobResponse[]>(apiConfig.paths.adminPendingJobs, auth(accessToken))
      return jobs.map(mapJobResponse)
    },
    async approveJob(accessToken: string, id: number) {
      const job = await requestApi<JobResponse>(endpoint(apiConfig.paths.adminApproveJob, { id }), {
        ...auth(accessToken),
        method: 'PATCH',
      })
      return mapJobResponse(job)
    },
    async rejectJob(accessToken: string, id: number) {
      const job = await requestApi<JobResponse>(endpoint(apiConfig.paths.adminRejectJob, { id }), {
        ...auth(accessToken),
        method: 'PATCH',
      })
      return mapJobResponse(job)
    },
  }
}

function auth(accessToken: string): RequestInit {
  return {
    headers: { Authorization: `Bearer ${accessToken}` },
  }
}
