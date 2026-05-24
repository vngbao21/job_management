import type { JobRepository } from '../../domain/repositories/jobRepository'
import { apiConfig } from '../config/apiConfig'
import { requestApi } from '../http/apiClient'
import { mapJobResponse, type JobResponse } from './jobMapper'

interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export function createHttpJobRepository(): JobRepository {
  return {
    async getApprovedJobs(params) {
      const query = new URLSearchParams({
        page: String(params.page),
        size: String(params.size),
      })

      if (params.keyword.trim()) {
        query.set('keyword', params.keyword.trim())
      }

      if (params.location.trim()) {
        query.set('location', params.location.trim())
      }

      if (params.jobType !== 'All') {
        query.set('jobType', params.jobType)
      }

      const page = await requestApi<PageResponse<JobResponse>>(`${apiConfig.paths.publicJobs}?${query}`)
      return {
        ...page,
        content: page.content.map(mapJobResponse),
      }
    },
    async getApprovedJobDetail(id: number) {
      const job = await requestApi<JobResponse>(`${apiConfig.paths.publicJobs}/${id}`)
      return mapJobResponse(job)
    },
  }
}
