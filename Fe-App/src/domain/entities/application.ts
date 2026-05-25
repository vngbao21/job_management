export interface CandidateApplicationForm {
  fullName: string
  email: string
  phone: string
  coverLetter: string
  cvUrl: string
  cvFileName: string
}

export type ApplicationStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED'

export interface JobApplication {
  id: number
  jobId: number
  jobTitle: string
  companyId?: number
  companyName: string
  candidateId?: number
  candidateName: string
  candidateEmail: string
  candidatePhone?: string
  cvUrl: string
  coverLetter: string
  status: ApplicationStatus
  createdAt: string
  updatedAt?: string
}

export interface ApplicationPayload {
  cvUrl: string
  coverLetter: string
}
