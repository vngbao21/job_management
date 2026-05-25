<script setup lang="ts">
import type { JobApplication } from '../../domain/entities/application'
import type { CompanyProfilePayload } from '../../domain/entities/company'
import type { CompanyDashboard } from '../../domain/entities/dashboard'
import type { Job, JobType } from '../../domain/entities/job'
import type { User } from '../../domain/entities/user'
import AdminPanel from './AdminPanel.vue'
import CandidatePanel from './CandidatePanel.vue'
import CompanyPanel from './CompanyPanel.vue'

defineProps<{
  adminPendingJobs: Job[]
  adminUsers: User[]
  candidateApplications: JobApplication[]
  companyApplications: JobApplication[]
  companyDashboard: CompanyDashboard | null
  companyJobForm: {
    editingJobId: number | null
    title: string
    description: string
    requirement: string
    salaryMin: number
    salaryMax: number
    location: string
    jobType: JobType
  }
  companyJobs: Job[]
  companyProfileForm: CompanyProfilePayload
  currentUser: User | null
  dashboardStats: {
    approvedJobs: number
    pendingJobs: number
    applications: number
    activeUsers: number
    totalUsers: number
  }
  loading: boolean
  message: string
}>()

defineEmits<{
  approveJob: [id: number]
  deleteCompanyJob: [id: number]
  editCompanyJob: [job: Job]
  refreshRoleData: []
  rejectJob: [id: number]
  refreshAdminUsers: []
  refreshCandidateApplications: []
  refreshCompanyApplications: []
  resetCompanyJobForm: []
  reviewApplication: [id: number, status: 'ACCEPTED' | 'REJECTED']
  saveCompanyJob: []
  saveCompanyProfile: []
  toggleUserStatus: [user: User]
}>()
</script>

<template>
  <section v-if="currentUser" id="dashboard" class="role-dashboard">
    <div class="dashboard-intro">
      <p class="eyebrow">Signed in as {{ currentUser.role }}</p>
      <h2>{{ currentUser.fullName }}</h2>
    </div>

    <AdminPanel
      v-if="currentUser.role === 'ADMIN'"
      :dashboard-stats="dashboardStats"
      :loading="loading"
      :message="message"
      :pending-jobs="adminPendingJobs"
      :users="adminUsers"
      @approve="$emit('approveJob', $event)"
      @refresh="$emit('refreshRoleData')"
      @refresh-users="$emit('refreshAdminUsers')"
      @reject="$emit('rejectJob', $event)"
      @toggle-user-status="$emit('toggleUserStatus', $event)"
    />

    <CompanyPanel
      v-else-if="currentUser.role === 'COMPANY'"
      :applications="companyApplications"
      :company-job-form="companyJobForm"
      :company-jobs="companyJobs"
      :company-profile-form="companyProfileForm"
      :dashboard="companyDashboard"
      :loading="loading"
      :message="message"
      @delete-job="$emit('deleteCompanyJob', $event)"
      @edit-job="$emit('editCompanyJob', $event)"
      @refresh-applications="$emit('refreshCompanyApplications')"
      @review-application="$emit('reviewApplication', $event.id, $event.status)"
      @reset-job-form="$emit('resetCompanyJobForm')"
      @save-job="$emit('saveCompanyJob')"
      @save-profile="$emit('saveCompanyProfile')"
    />

    <CandidatePanel
      v-else
      :applications="candidateApplications"
      :loading="loading"
      :message="message"
      @refresh-applications="$emit('refreshCandidateApplications')"
    />
  </section>
</template>
