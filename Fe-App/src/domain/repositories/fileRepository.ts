import type { FileUploadResult } from '../entities/file'

export interface FileRepository {
  uploadCv(accessToken: string, file: File): Promise<FileUploadResult>
}
