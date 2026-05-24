import { computed, onMounted, reactive, ref } from 'vue'
import type { ApplicationStatus, CandidateApplicationForm, JobApplication } from '../domain/entities/application'
import type { CompanyProfile, CompanyProfilePayload } from '../domain/entities/company'
import type { Job, JobFilters, JobPage, JobType } from '../domain/entities/job'
import type { Role, User } from '../domain/entities/user'
import type { ApplicationRepository } from '../domain/repositories/applicationRepository'
import type { AdminRepository } from '../domain/repositories/adminRepository'
import type { AuthRepository } from '../domain/repositories/authRepository'
import type { CompanyRepository } from '../domain/repositories/companyRepository'
import type { HealthRepository } from '../domain/repositories/healthRepository'
import type { JobRepository } from '../domain/repositories/jobRepository'
import { demoJobs } from '../infrastructure/data/demoJobs'
import { createHttpApplicationRepository } from '../infrastructure/repositories/httpApplicationRepository'
import { createHttpAdminRepository } from '../infrastructure/repositories/httpAdminRepository'
import { createHttpAuthRepository } from '../infrastructure/repositories/httpAuthRepository'
import { createHttpCompanyRepository } from '../infrastructure/repositories/httpCompanyRepository'
import { createHttpHealthRepository } from '../infrastructure/repositories/httpHealthRepository'
import { createHttpJobRepository } from '../infrastructure/repositories/httpJobRepository'
import { tokenStorage } from '../infrastructure/storage/tokenStorage'

type AuthMode = 'login' | 'register'
type ToastType = 'success' | 'error' | 'info'

interface ToastState {
  id: number
  type: ToastType
  title: string
  message: string
}

interface Dependencies {
  applicationRepository?: ApplicationRepository
  adminRepository?: AdminRepository
  authRepository?: AuthRepository
  companyRepository?: CompanyRepository
  healthRepository?: HealthRepository
  jobRepository?: JobRepository
}

export function useRecruitmentPage(dependencies: Dependencies = {}) {
  const applicationRepository = dependencies.applicationRepository ?? createHttpApplicationRepository()
  const adminRepository = dependencies.adminRepository ?? createHttpAdminRepository()
  const authRepository = dependencies.authRepository ?? createHttpAuthRepository()
  const companyRepository = dependencies.companyRepository ?? createHttpCompanyRepository()
  const healthRepository = dependencies.healthRepository ?? createHttpHealthRepository()
  const jobRepository = dependencies.jobRepository ?? createHttpJobRepository()

  const jobs = ref<Job[]>(demoJobs)
  const selectedJobId = ref(jobs.value[0].id)
  const apiStatus = ref('Checking backend...')
  const authMode = ref<AuthMode>('login')
  const authOpen = ref(false)
  const authLoading = ref(false)
  const authMessage = ref('')
  const authMessageType = ref<'success' | 'error' | 'info'>('info')
  const currentUser = ref<User | null>(null)
  const initialLoading = ref(true)
  const roleActionLoading = ref(false)
  const roleActionMessage = ref('')
  const toast = ref<ToastState | null>(null)
  const token = ref(tokenStorage.get())
  const adminPendingJobs = ref<Job[]>([])
  const companyProfile = ref<CompanyProfile | null>(null)
  const companyJobs = ref<Job[]>([])
  const candidateApplications = ref<JobApplication[]>([])
  const companyApplications = ref<JobApplication[]>([])
  const managedUsers = ref<User[]>([
    {
      id: 1,
      email: 'admin@example.com',
      fullName: 'Admin Demo',
      phone: '0900000001',
      role: 'ADMIN',
      status: 'ACTIVE',
    },
    {
      id: 2,
      email: 'company@example.com',
      fullName: 'Company Demo',
      phone: '0900000002',
      role: 'COMPANY',
      status: 'ACTIVE',
    },
    {
      id: 3,
      email: 'candidate@example.com',
      fullName: 'Candidate Demo',
      phone: '0900000003',
      role: 'CANDIDATE',
      status: 'ACTIVE',
    },
  ])

  const filters = reactive<JobFilters>({
    keyword: '',
    location: '',
    jobType: 'All',
  })

  const jobPage = reactive<JobPage>({
    content: demoJobs,
    page: 0,
    size: 10,
    totalElements: demoJobs.length,
    totalPages: 1,
    last: true,
  })

  const authForm = reactive({
    email: 'candidate@example.com',
    password: '123456',
    fullName: 'Candidate Demo',
    phone: '0900000000',
    role: 'CANDIDATE' as Role,
  })

  const applicationForm = reactive<CandidateApplicationForm>({
    fullName: '',
    email: '',
    phone: '',
    coverLetter:
      'I have built Spring Boot APIs with JWT, JPA, and MySQL. I want to contribute to your recruitment product and can share my portfolio during the interview.',
    cvName: '',
  })

  const companyProfileForm = reactive<CompanyProfilePayload>({
    companyName: 'Demo Tech Company',
    website: 'https://example.com',
    address: 'Ho Chi Minh City',
    description: 'A product engineering team hiring Java and fullstack developers.',
  })

  const companyJobForm = reactive({
    editingJobId: null as number | null,
    title: 'Java Spring Boot Developer',
    description: 'Build recruitment APIs, integrate business workflows, and ship production-ready backend features.',
    requirement: 'Java, Spring Boot, Spring Security, JPA, MySQL, REST API, Git.',
    salaryMin: 1200,
    salaryMax: 2500,
    location: 'Ho Chi Minh',
    jobType: 'FULL_TIME' as JobType,
  })

  const selectedJob = computed(() => jobs.value.find((job) => job.id === selectedJobId.value) || jobs.value[0] || null)
  const salaryText = computed(() => formatSalary(selectedJob.value))
  const filteredJobs = computed(() => jobs.value)
  const globalLoading = computed(() => initialLoading.value || authLoading.value || roleActionLoading.value)
  const globalLoadingText = computed(() => {
    if (authLoading.value) {
      return authMode.value === 'login' ? 'Signing in...' : 'Creating account...'
    }

    if (roleActionLoading.value) {
      return 'Processing request...'
    }

    return 'Loading workspace...'
  })
  const dashboardStats = computed(() => ({
    approvedJobs: jobs.value.filter((job) => job.status === 'APPROVED').length,
    pendingJobs: adminPendingJobs.value.length,
    applications: candidateApplications.value.length + companyApplications.value.length,
    activeUsers: managedUsers.value.filter((user) => user.status === 'ACTIVE').length,
  }))

  onMounted(async () => {
    try {
      const backendConnected = await checkHealth()
      if (backendConnected) {
        await loadApprovedJobs()
      }

      if (token.value) {
        await loadCurrentUser()
      }
    } finally {
      initialLoading.value = false
    }
  })

  async function checkHealth() {
    try {
      const backendConnected = await healthRepository.check()
      apiStatus.value = backendConnected ? 'Backend connected' : 'Backend responded with an error'
      if (!backendConnected) {
        notify('error', 'Backend error', 'The backend responded, but the health check failed.')
      }
      return backendConnected
    } catch {
      apiStatus.value = 'Backend offline, showing demo jobs'
      notify('info', 'Demo mode enabled', 'The backend is offline, so demo jobs are being shown.')
      return false
    }
  }

  async function loadApprovedJobs(page = jobPage.page) {
    try {
      const approvedJobs = await jobRepository.getApprovedJobs({
        keyword: filters.keyword,
        location: filters.location,
        jobType: filters.jobType,
        page,
        size: jobPage.size,
      })
      jobs.value = approvedJobs.content
      jobPage.content = approvedJobs.content
      jobPage.page = approvedJobs.page
      jobPage.size = approvedJobs.size
      jobPage.totalElements = approvedJobs.totalElements
      jobPage.totalPages = approvedJobs.totalPages
      jobPage.last = approvedJobs.last

      if (approvedJobs.content.length > 0) {
        selectedJobId.value = approvedJobs.content[0].id
        apiStatus.value = 'Loaded approved jobs from API'
        notify('success', 'Jobs loaded', 'Approved jobs were loaded from the API.')
      } else {
        selectedJobId.value = 0
        apiStatus.value = 'Backend connected, no approved jobs yet'
        notify('info', 'No matching jobs', 'Try changing the search keyword, location, or job type.')
      }
    } catch {
      apiStatus.value = 'Cannot load jobs, showing demo data'
      jobs.value = demoJobs
      selectedJobId.value = demoJobs[0].id
      notify('error', 'Cannot load jobs', 'Demo jobs are shown because the public jobs API failed.')
    }
  }

  async function searchJobs() {
    roleActionLoading.value = true
    try {
      await loadApprovedJobs(0)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function changeJobPage(page: number) {
    if (page < 0 || page >= jobPage.totalPages) {
      return
    }

    roleActionLoading.value = true
    try {
      await loadApprovedJobs(page)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function resetJobSearch() {
    filters.keyword = ''
    filters.location = ''
    filters.jobType = 'All'
    await searchJobs()
  }

  async function loadCurrentUser() {
    try {
      const user = await authRepository.me(token.value)
      setCurrentUser(user)
    } catch {
      token.value = ''
      tokenStorage.clear()
      notify('error', 'Session expired', 'Please sign in again.')
    }
  }

  async function submitAuth() {
    authLoading.value = true
    authMessage.value = ''
    authMessageType.value = 'info'

    try {
      if (authMode.value === 'login') {
        const login = await authRepository.login({
          email: authForm.email,
          password: authForm.password,
        })

        token.value = login.accessToken
        tokenStorage.set(login.accessToken)
        setCurrentUser(login.user)
        authOpen.value = false
        notify('success', 'Login successful', `Welcome back, ${login.user.fullName}.`)
      } else {
        await authRepository.register({
          email: authForm.email,
          password: authForm.password,
          fullName: authForm.fullName,
          phone: authForm.phone,
          role: authForm.role,
        })
        authMode.value = 'login'
        authMessage.value = 'Account created. You can sign in now.'
        authMessageType.value = 'success'
        notify('success', 'Account created', 'You can sign in with the new account.')
      }
    } catch (error) {
      authMessage.value = error instanceof Error ? error.message : 'Cannot connect to backend'
      authMessageType.value = 'error'
      notify('error', authMode.value === 'login' ? 'Login failed' : 'Registration failed', authMessage.value)
    } finally {
      authLoading.value = false
    }
  }

  function setCurrentUser(user: User) {
    currentUser.value = user
    applicationForm.fullName = user.fullName
    applicationForm.email = user.email
    applicationForm.phone = user.phone || ''
    void loadRoleData()
  }

  function logout() {
    token.value = ''
    currentUser.value = null
    adminPendingJobs.value = []
    companyJobs.value = []
    companyProfile.value = null
    tokenStorage.clear()
    notify('info', 'Signed out', 'Your local session has been cleared.')
  }

  function selectJob(id: number) {
    selectedJobId.value = id
  }

  function handleCvChange(fileName: string) {
    applicationForm.cvName = fileName
  }

  function requestAuth(message = '') {
    authOpen.value = true
    authMessage.value = message
    authMessageType.value = message ? 'info' : 'info'
  }

  async function submitApplication() {
    if (!currentUser.value) {
      requestAuth('Sign in as candidate before applying.')
      return
    }

    if (currentUser.value.role !== 'CANDIDATE') {
      roleActionMessage.value = 'Only candidate accounts can apply for jobs.'
      notify('error', 'Cannot apply', roleActionMessage.value)
      return
    }

    if (!selectedJob.value) {
      roleActionMessage.value = 'Select a job before applying.'
      notify('error', 'No job selected', roleActionMessage.value)
      return
    }

    const alreadyApplied = candidateApplications.value.some(
      (application) => application.jobId === selectedJob.value?.id && application.candidateEmail === currentUser.value?.email,
    )

    if (alreadyApplied) {
      roleActionMessage.value = 'You already applied for this job.'
      notify('info', 'Application already exists', roleActionMessage.value)
      return
    }

    if (!token.value) {
      requestAuth('Sign in as candidate before applying.')
      return
    }

    roleActionLoading.value = true
    try {
      const application = await applicationRepository.applyJob(token.value, selectedJob.value.id, {
        cvUrl: applicationForm.cvName,
        coverLetter: applicationForm.coverLetter,
      })
      candidateApplications.value = [application, ...candidateApplications.value]
      roleActionMessage.value = 'Application submitted. It is pending company review.'
      notify('success', 'Application submitted', 'Your application was sent to the company.')
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Application failed', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  function setAuthMode(nextMode: AuthMode) {
    authMode.value = nextMode
    authMessage.value = ''
  }

  async function loadRoleData() {
    if (!currentUser.value || !token.value) {
      return
    }

    roleActionMessage.value = ''
    if (currentUser.value.role === 'ADMIN') {
      await loadAdminPendingJobs()
    }

    if (currentUser.value.role === 'COMPANY') {
      await Promise.all([loadCompanyProfile(), loadCompanyJobs()])
      await loadCompanyApplications()
    }

    if (currentUser.value.role === 'CANDIDATE') {
      await loadCandidateApplications()
    }
  }

  async function loadAdminPendingJobs() {
    if (!token.value) {
      return
    }

    roleActionLoading.value = true
    try {
      adminPendingJobs.value = await adminRepository.getPendingJobs(token.value)
      roleActionMessage.value = `Loaded ${adminPendingJobs.value.length} pending job(s).`
      notify('success', 'Pending jobs loaded', roleActionMessage.value)
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot load pending jobs', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function approveJob(id: number) {
    await moderateJob(id, 'approve')
  }

  async function rejectJob(id: number) {
    await moderateJob(id, 'reject')
  }

  async function moderateJob(id: number, action: 'approve' | 'reject') {
    if (!token.value) {
      requestAuth('Sign in as admin first.')
      return
    }

    roleActionLoading.value = true
    try {
      if (action === 'approve') {
        await adminRepository.approveJob(token.value, id)
      } else {
        await adminRepository.rejectJob(token.value, id)
      }
      roleActionMessage.value = action === 'approve' ? 'Job approved successfully.' : 'Job rejected successfully.'
      notify('success', action === 'approve' ? 'Job approved' : 'Job rejected', roleActionMessage.value)
      await loadAdminPendingJobs()
      await loadApprovedJobs()
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Moderation failed', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function loadCompanyProfile() {
    if (!token.value) {
      return
    }

    try {
      const profile = await companyRepository.getProfile(token.value)
      companyProfile.value = profile
      companyProfileForm.companyName = profile.companyName
      companyProfileForm.website = profile.website || ''
      companyProfileForm.address = profile.address || ''
      companyProfileForm.description = profile.description || ''
    } catch {
      companyProfile.value = null
    }
  }

  async function saveCompanyProfile() {
    if (!token.value) {
      requestAuth('Sign in as company first.')
      return
    }

    roleActionLoading.value = true
    try {
      companyProfile.value = companyProfile.value
        ? await companyRepository.updateProfile(token.value, companyProfileForm)
        : await companyRepository.createProfile(token.value, companyProfileForm)
      roleActionMessage.value = 'Company profile saved.'
      notify('success', 'Profile saved', 'Company profile changes were saved.')
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot save profile', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function loadCompanyJobs() {
    if (!token.value) {
      return
    }

    try {
      companyJobs.value = await companyRepository.getJobs(token.value)
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot load company jobs', roleActionMessage.value)
    }
  }

  async function loadCandidateApplications() {
    if (!token.value) {
      return
    }

    roleActionLoading.value = true
    try {
      candidateApplications.value = await applicationRepository.getCandidateApplications(token.value)
      roleActionMessage.value = `Loaded ${candidateApplications.value.length} application(s).`
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot load applications', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function loadCompanyApplications() {
    if (!token.value || companyJobs.value.length === 0) {
      companyApplications.value = []
      return
    }

    roleActionLoading.value = true
    try {
      const applicationGroups = await Promise.all(
        companyJobs.value.map((job) => applicationRepository.getCompanyJobApplications(token.value, job.id)),
      )
      companyApplications.value = applicationGroups.flat()
      roleActionMessage.value = `Loaded ${companyApplications.value.length} candidate application(s).`
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot load candidate applications', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function saveCompanyJob() {
    if (!token.value) {
      requestAuth('Sign in as company first.')
      return
    }

    if (Number(companyJobForm.salaryMin) > Number(companyJobForm.salaryMax)) {
      roleActionMessage.value = 'Salary min must be less than or equal to salary max.'
      notify('error', 'Invalid salary range', roleActionMessage.value)
      return
    }

    roleActionLoading.value = true
    try {
      const payload = {
        title: companyJobForm.title,
        description: companyJobForm.description,
        requirement: companyJobForm.requirement,
        salaryMin: Number(companyJobForm.salaryMin),
        salaryMax: Number(companyJobForm.salaryMax),
        location: companyJobForm.location,
        jobType: companyJobForm.jobType,
      }

      if (companyJobForm.editingJobId) {
        await companyRepository.updateJob(token.value, companyJobForm.editingJobId, payload)
        roleActionMessage.value = 'Job updated. It keeps its current approval status.'
        notify('success', 'Job updated', roleActionMessage.value)
      } else {
        await companyRepository.createJob(token.value, payload)
        roleActionMessage.value = 'Job created. Status is PENDING until admin approval.'
        notify('success', 'Job created', roleActionMessage.value)
      }

      resetCompanyJobForm()
      await loadCompanyJobs()
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot save job', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function deleteCompanyJob(id: number) {
    if (!token.value) {
      return
    }

    roleActionLoading.value = true
    try {
      await companyRepository.deleteJob(token.value, id)
      roleActionMessage.value = 'Job deleted.'
      notify('success', 'Job deleted', 'The job was removed from your company list.')
      await loadCompanyJobs()
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Cannot delete job', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  async function reviewApplication(id: number, status: Exclude<ApplicationStatus, 'PENDING'>) {
    if (!token.value) {
      requestAuth('Sign in as company first.')
      return
    }

    roleActionLoading.value = true
    try {
      const updatedApplication = await applicationRepository.reviewApplication(token.value, id, status)
      companyApplications.value = companyApplications.value.map((application) =>
        application.id === id ? updatedApplication : application,
      )
      candidateApplications.value = candidateApplications.value.map((application) =>
        application.id === id ? updatedApplication : application,
      )
      roleActionMessage.value = status === 'ACCEPTED' ? 'Application accepted.' : 'Application rejected.'
      notify('success', status === 'ACCEPTED' ? 'Application accepted' : 'Application rejected', roleActionMessage.value)
    } catch (error) {
      roleActionMessage.value = getErrorMessage(error)
      notify('error', 'Review failed', roleActionMessage.value)
    } finally {
      roleActionLoading.value = false
    }
  }

  function toggleUserStatus(id: number) {
    managedUsers.value = managedUsers.value.map((user) =>
      user.id === id ? { ...user, status: user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE' } : user,
    )
    roleActionMessage.value = 'User status updated.'
    notify('success', 'User updated', roleActionMessage.value)
  }

  function dismissToast() {
    toast.value = null
  }

  function editCompanyJob(job: Job) {
    companyJobForm.editingJobId = job.id
    companyJobForm.title = job.title
    companyJobForm.description = job.description
    companyJobForm.requirement = job.requirement
    companyJobForm.salaryMin = job.salaryMin
    companyJobForm.salaryMax = job.salaryMax
    companyJobForm.location = job.location
    companyJobForm.jobType = job.jobType
  }

  function resetCompanyJobForm() {
    companyJobForm.editingJobId = null
    companyJobForm.title = ''
    companyJobForm.description = ''
    companyJobForm.requirement = ''
    companyJobForm.salaryMin = 0
    companyJobForm.salaryMax = 0
    companyJobForm.location = ''
    companyJobForm.jobType = 'FULL_TIME'
  }

  return {
    apiStatus,
    applicationForm,
    authForm,
    authLoading,
    authMessage,
    authMessageType,
    authMode,
    authOpen,
    adminPendingJobs,
    candidateApplications,
    companyApplications,
    companyJobForm,
    companyJobs,
    companyProfile,
    companyProfileForm,
    currentUser,
    dashboardStats,
    filteredJobs,
    filters,
    globalLoading,
    globalLoadingText,
    initialLoading,
    jobPage,
    jobs,
    roleActionLoading,
    roleActionMessage,
    salaryText,
    selectedJob,
    selectedJobId,
    toast,
    approveJob,
    changeJobPage,
    deleteCompanyJob,
    dismissToast,
    editCompanyJob,
    handleCvChange,
    loadCandidateApplications,
    loadCompanyApplications,
    loadRoleData,
    logout,
    requestAuth,
    rejectJob,
    resetJobSearch,
    resetCompanyJobForm,
    reviewApplication,
    selectJob,
    setAuthMode,
    saveCompanyJob,
    saveCompanyProfile,
    searchJobs,
    submitApplication,
    submitAuth,
    toggleUserStatus,
    managedUsers,
  }

  function notify(type: ToastType, title: string, message: string) {
    const id = Date.now()
    toast.value = { id, type, title, message }
    window.setTimeout(() => {
      if (toast.value?.id === id) {
        toast.value = null
      }
    }, 3600)
  }

}

function formatSalary(job: Job | null) {
  if (!job) {
    return ''
  }

  return `$${job.salaryMin.toLocaleString()} - $${job.salaryMax.toLocaleString()}`
}

function getErrorMessage(error: unknown) {
  return error instanceof Error ? error.message : 'Action failed'
}

export type { AuthMode, JobType }
