import type { HealthRepository } from '../../domain/repositories/healthRepository'
import { apiConfig } from '../config/apiConfig'
import { apiBaseUrl } from '../http/apiClient'

export function createHttpHealthRepository(): HealthRepository {
  return {
    async check() {
      const response = await fetch(`${apiBaseUrl}${apiConfig.paths.health}`)
      return response.ok
    },
  }
}
