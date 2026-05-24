import type { ApplicationPayload, ApplicationStatus } from '../../domain/entities/application'
import type { ApplicationRepository } from '../../domain/repositories/applicationRepository'
import { apiConfig, endpoint } from '../config/apiConfig'
import { requestApi } from '../http/apiClient'
import { mapApplicationResponse, type ApplicationResponse } from './applicationMapper'

export function createHttpApplicationRepository(): ApplicationRepository {
  return {
    async applyJob(accessToken: string, jobId: number, payload: ApplicationPayload) {
      const application = await requestApi<ApplicationResponse>(endpoint(apiConfig.paths.applyJob, { id: jobId }), {
        ...auth(accessToken),
        method: 'POST',
        body: JSON.stringify(payload),
      })
      return mapApplicationResponse(application)
    },
    async getCandidateApplications(accessToken: string) {
      const applications = await requestApi<ApplicationResponse[]>(
        apiConfig.paths.candidateApplications,
        auth(accessToken),
      )
      return applications.map(mapApplicationResponse)
    },
    async getCompanyJobApplications(accessToken: string, jobId: number) {
      const applications = await requestApi<ApplicationResponse[]>(
        endpoint(apiConfig.paths.companyJobApplications, { id: jobId }),
        auth(accessToken),
      )
      return applications.map(mapApplicationResponse)
    },
    async reviewApplication(
      accessToken: string,
      id: number,
      status: Exclude<ApplicationStatus, 'PENDING'>,
    ) {
      const path =
        status === 'ACCEPTED' ? apiConfig.paths.companyAcceptApplication : apiConfig.paths.companyRejectApplication
      const application = await requestApi<ApplicationResponse>(endpoint(path, { id }), {
        ...auth(accessToken),
        method: 'PATCH',
      })
      return mapApplicationResponse(application)
    },
  }
}

function auth(accessToken: string): RequestInit {
  return {
    headers: { Authorization: `Bearer ${accessToken}` },
  }
}
