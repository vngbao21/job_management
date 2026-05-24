import type { AuthRepository } from '../../domain/repositories/authRepository'
import type { LoginCredentials, LoginResult, RegisterPayload, User } from '../../domain/entities/user'
import { apiConfig } from '../config/apiConfig'
import { requestApi } from '../http/apiClient'

export function createHttpAuthRepository(): AuthRepository {
  return {
    login(credentials: LoginCredentials) {
      return requestApi<LoginResult>(apiConfig.paths.authLogin, {
        method: 'POST',
        body: JSON.stringify(credentials),
      })
    },
    register(payload: RegisterPayload) {
      return requestApi<User>(apiConfig.paths.authRegister, {
        method: 'POST',
        body: JSON.stringify(payload),
      })
    },
    me(accessToken: string) {
      return requestApi<User>(apiConfig.paths.authMe, {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
    },
  }
}
