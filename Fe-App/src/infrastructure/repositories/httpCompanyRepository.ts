import type { CompanyProfile, CompanyProfilePayload } from '../../domain/entities/company'
import type { JobPayload } from '../../domain/entities/job'
import type { CompanyRepository } from '../../domain/repositories/companyRepository'
import { apiConfig } from '../config/apiConfig'
import { requestApi } from '../http/apiClient'
import { mapJobResponse, type JobResponse } from './jobMapper'

export function createHttpCompanyRepository(): CompanyRepository {
  return {
    getProfile(accessToken: string) {
      return requestApi<CompanyProfile>(apiConfig.paths.companyProfile, auth(accessToken))
    },
    createProfile(accessToken: string, payload: CompanyProfilePayload) {
      return requestApi<CompanyProfile>(apiConfig.paths.companyProfile, {
        ...auth(accessToken),
        method: 'POST',
        body: JSON.stringify(payload),
      })
    },
    updateProfile(accessToken: string, payload: CompanyProfilePayload) {
      return requestApi<CompanyProfile>(apiConfig.paths.companyProfile, {
        ...auth(accessToken),
        method: 'PUT',
        body: JSON.stringify(payload),
      })
    },
    async getJobs(accessToken: string) {
      const jobs = await requestApi<JobResponse[]>(apiConfig.paths.companyJobs, auth(accessToken))
      return jobs.map(mapJobResponse)
    },
    async createJob(accessToken: string, payload: JobPayload) {
      const job = await requestApi<JobResponse>(apiConfig.paths.companyJobs, {
        ...auth(accessToken),
        method: 'POST',
        body: JSON.stringify(payload),
      })
      return mapJobResponse(job)
    },
    async updateJob(accessToken: string, id: number, payload: JobPayload) {
      const job = await requestApi<JobResponse>(`${apiConfig.paths.companyJobs}/${id}`, {
        ...auth(accessToken),
        method: 'PUT',
        body: JSON.stringify(payload),
      })
      return mapJobResponse(job)
    },
    async deleteJob(accessToken: string, id: number) {
      await requestApi<void>(`${apiConfig.paths.companyJobs}/${id}`, {
        ...auth(accessToken),
        method: 'DELETE',
      })
    },
  }
}

function auth(accessToken: string): RequestInit {
  return {
    headers: { Authorization: `Bearer ${accessToken}` },
  }
}
