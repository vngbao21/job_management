import type { JobApplication } from '../../domain/entities/application'

export interface ApplicationResponse {
  id: number
  jobId: number
  jobTitle: string
  companyId: number
  companyName: string
  candidateId: number
  candidateName: string
  candidateEmail: string
  cvUrl: string | null
  coverLetter: string | null
  status: JobApplication['status']
  createdAt: string
  updatedAt: string
}

export function mapApplicationResponse(response: ApplicationResponse): JobApplication {
  const cvUrl = response.cvUrl || ''

  return {
    id: response.id,
    jobId: response.jobId,
    jobTitle: response.jobTitle,
    companyId: response.companyId,
    companyName: response.companyName,
    candidateId: response.candidateId,
    candidateName: response.candidateName,
    candidateEmail: response.candidateEmail,
    cvUrl,
    cvName: getFileName(cvUrl),
    coverLetter: response.coverLetter || '',
    status: response.status,
    createdAt: response.createdAt,
    updatedAt: response.updatedAt,
  }
}

function getFileName(cvUrl: string) {
  if (!cvUrl) {
    return 'No CV attached'
  }

  return cvUrl.split(/[\\/]/).pop() || cvUrl
}
