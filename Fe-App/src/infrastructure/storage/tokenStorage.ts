const tokenKey = 'jm_access_token'
const userKey = 'jm_current_user'

export const tokenStorage = {
  get() {
    return localStorage.getItem(tokenKey) || ''
  },
  set(accessToken: string) {
    localStorage.setItem(tokenKey, accessToken)
  },
  getUserRole() {
    try {
      const user = JSON.parse(localStorage.getItem(userKey) || 'null') as { role?: string } | null
      return user?.role || ''
    } catch {
      return ''
    }
  },
  setUser(user: unknown) {
    localStorage.setItem(userKey, JSON.stringify(user))
  },
  clear() {
    localStorage.removeItem(tokenKey)
    localStorage.removeItem(userKey)
  },
}
