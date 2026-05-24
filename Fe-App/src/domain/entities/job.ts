export type JobType = 'FULL_TIME' | 'REMOTE' | 'INTERNSHIP' | 'CONTRACT' | 'PART_TIME'
export type JobStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CLOSED'

export interface Job {
  id: number
  companyName: string
  title: string
  description: string
  requirement: string
  salaryMin: number
  salaryMax: number
  location: string
  jobType: JobType
  status: JobStatus
  tags: string[]
  highlight: string
  postedAt: string
}

export interface JobFilters {
  keyword: string
  location: string
  jobType: 'All' | JobType
}

export interface JobPage {
  content: Job[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface JobSearchParams extends JobFilters {
  page: number
  size: number
}

export interface JobPayload {
  title: string
  description: string
  requirement: string
  salaryMin: number
  salaryMax: number
  location: string
  jobType: JobType
}
