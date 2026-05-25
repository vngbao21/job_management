import type { FileUploadResult } from '../../domain/entities/file'
import type { FileRepository } from '../../domain/repositories/fileRepository'
import { apiConfig } from '../config/apiConfig'
import { apiBaseUrl, type ApiEnvelope } from '../http/apiClient'

export function createHttpFileRepository(): FileRepository {
  return {
    async uploadCv(accessToken: string, file: File) {
      const body = new FormData()
      body.append('file', file)

      const response = await fetch(`${apiBaseUrl}${apiConfig.paths.uploadCv}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
        body,
      })

      const payload = (await response.json()) as ApiEnvelope<FileUploadResult>
      if (!response.ok || !payload.success) {
        throw new Error(payload.message || `Upload failed with status ${response.status}`)
      }

      return payload.data
    },
  }
}
