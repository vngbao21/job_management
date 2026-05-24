export interface ApiEnvelope<T> {
  success: boolean
  message: string
  data: T
}

import { apiConfig } from '../config/apiConfig'

export const apiBaseUrl = apiConfig.baseUrl

export async function requestApi<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${apiBaseUrl}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...init?.headers,
    },
  })
  let body: ApiEnvelope<T>
  try {
    body = (await response.json()) as ApiEnvelope<T>
  } catch {
    throw new Error(`Request failed with status ${response.status}`)
  }

  if (!response.ok || !body.success) {
    throw new Error(body.message || `Request failed with status ${response.status}`)
  }

  return body.data
}
