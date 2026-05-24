import type { LoginCredentials, LoginResult, RegisterPayload, User } from '../entities/user'

export interface AuthRepository {
  login(credentials: LoginCredentials): Promise<LoginResult>
  register(payload: RegisterPayload): Promise<User>
  me(accessToken: string): Promise<User>
}
