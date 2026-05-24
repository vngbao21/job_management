const tokenKey = 'jm_access_token'

export const tokenStorage = {
  get() {
    return localStorage.getItem(tokenKey) || ''
  },
  set(accessToken: string) {
    localStorage.setItem(tokenKey, accessToken)
  },
  clear() {
    localStorage.removeItem(tokenKey)
  },
}
