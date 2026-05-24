export interface CompanyProfile {
  id: number
  userId: number
  companyName: string
  website: string
  address: string
  description: string
  createdAt: string
  updatedAt: string
}

export interface CompanyProfilePayload {
  companyName: string
  website: string
  address: string
  description: string
}
