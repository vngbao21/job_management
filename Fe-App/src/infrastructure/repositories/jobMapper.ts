import type { Job } from '../../domain/entities/job'

export interface JobResponse {
  id: number
  companyId: number
  companyName: string
  title: string
  description: string
  requirement: string
  salaryMin: number | string | null
  salaryMax: number | string | null
  location: string
  jobType: Job['jobType']
  status: Job['status']
  createdAt: string
  updatedAt: string
}

export function mapJobResponse(response: JobResponse): Job {
  return {
    id: response.id,
    companyName: response.companyName,
    title: response.title,
    description: response.description,
    requirement: response.requirement || 'Requirements will be updated by the company.',
    salaryMin: Number(response.salaryMin ?? 0),
    salaryMax: Number(response.salaryMax ?? 0),
    location: response.location,
    jobType: response.jobType,
    status: response.status,
    tags: [response.jobType.replace('_', ' '), response.location, response.status],
    highlight: response.description,
    postedAt: formatPostedAt(response.createdAt),
  }
}

function formatPostedAt(createdAt: string) {
  if (!createdAt) {
    return 'Recently'
  }

  const createdTime = new Date(createdAt).getTime()
  if (Number.isNaN(createdTime)) {
    return 'Recently'
  }

  const days = Math.max(0, Math.floor((Date.now() - createdTime) / 86_400_000))
  return days === 0 ? 'Today' : `${days} day${days === 1 ? '' : 's'} ago`
}
