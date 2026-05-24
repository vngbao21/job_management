export type Role = 'CANDIDATE' | 'COMPANY' | 'ADMIN'

export interface User {
  id: number
  email: string
  fullName: string
  phone?: string
  role: Role
  status: string
}

export interface LoginCredentials {
  email: string
  password: string
}

export interface RegisterPayload extends LoginCredentials {
  fullName: string
  phone: string
  role: Role
}

export interface LoginResult {
  accessToken: string
  tokenType: string
  user: User
}
