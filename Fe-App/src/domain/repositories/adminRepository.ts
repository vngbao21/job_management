import type { AdminDashboard } from '../entities/dashboard'
import type { Job } from '../entities/job'
import type { User } from '../entities/user'

export interface AdminRepository {
  getDashboard(accessToken: string): Promise<AdminDashboard>
  getPendingJobs(accessToken: string): Promise<Job[]>
  approveJob(accessToken: string, id: number): Promise<Job>
  rejectJob(accessToken: string, id: number): Promise<Job>
  getUsers(accessToken: string): Promise<User[]>
  activateUser(accessToken: string, id: number): Promise<User>
  deactivateUser(accessToken: string, id: number): Promise<User>
}
