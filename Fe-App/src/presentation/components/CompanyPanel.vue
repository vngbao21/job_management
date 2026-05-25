<script setup lang="ts">
import type { JobApplication } from '../../domain/entities/application'
import type { CompanyProfilePayload } from '../../domain/entities/company'
import type { CompanyDashboard } from '../../domain/entities/dashboard'
import type { Job, JobType } from '../../domain/entities/job'

defineProps<{
  applications: JobApplication[]
  dashboard: CompanyDashboard | null
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
  loading: boolean
  message: string
}>()

defineEmits<{
  deleteJob: [id: number]
  editJob: [job: Job]
  refreshApplications: []
  reviewApplication: [payload: { id: number; status: 'ACCEPTED' | 'REJECTED' }]
  resetJobForm: []
  saveJob: []
  saveProfile: []
}>()
</script>

<template>
  <section class="role-panel company-panel">
    <div class="role-panel-head">
      <div>
        <p class="eyebrow">Company actions</p>
        <h2>Profile and job management</h2>
      </div>
    </div>

    <p v-if="message" class="form-message">{{ message }}</p>

    <div v-if="dashboard" class="admin-stats">
      <div><strong>{{ dashboard.totalJobs }}</strong><span>Total jobs</span></div>
      <div><strong>{{ dashboard.pendingJobs }}</strong><span>Pending jobs</span></div>
      <div><strong>{{ dashboard.approvedJobs }}</strong><span>Approved jobs</span></div>
      <div><strong>{{ dashboard.totalApplications }}</strong><span>Applications</span></div>
    </div>

    <div class="role-grid">
      <form class="manager-form" @submit.prevent="$emit('saveProfile')">
        <h3>Company profile</h3>
        <label>
          Company name
          <input v-model="companyProfileForm.companyName" required />
        </label>
        <label>
          Website
          <input v-model="companyProfileForm.website" />
        </label>
        <label>
          Address
          <input v-model="companyProfileForm.address" />
        </label>
        <label>
          Description
          <textarea v-model="companyProfileForm.description" rows="4"></textarea>
        </label>
        <button class="primary-button" type="submit" :disabled="loading">Save profile</button>
      </form>

      <form class="manager-form" @submit.prevent="$emit('saveJob')">
        <h3>{{ companyJobForm.editingJobId ? 'Edit job' : 'Create job' }}</h3>
        <label>
          Title
          <input v-model="companyJobForm.title" required />
        </label>
        <label>
          Location
          <input v-model="companyJobForm.location" required />
        </label>
        <label>
          Job type
          <select v-model="companyJobForm.jobType">
            <option value="FULL_TIME">Full time</option>
            <option value="REMOTE">Remote</option>
            <option value="INTERNSHIP">Internship</option>
            <option value="CONTRACT">Contract</option>
            <option value="PART_TIME">Part time</option>
          </select>
        </label>
        <div class="split-fields">
          <label>
            Salary min
            <input v-model.number="companyJobForm.salaryMin" min="0" type="number" />
          </label>
          <label>
            Salary max
            <input v-model.number="companyJobForm.salaryMax" min="0" type="number" />
          </label>
        </div>
        <label>
          Description
          <textarea v-model="companyJobForm.description" required rows="4"></textarea>
        </label>
        <label>
          Requirement
          <textarea v-model="companyJobForm.requirement" rows="4"></textarea>
        </label>
        <div class="action-row">
          <button class="primary-button" type="submit" :disabled="loading">
            {{ companyJobForm.editingJobId ? 'Update job' : 'Create job' }}
          </button>
          <button class="ghost-button" type="button" @click="$emit('resetJobForm')">Clear</button>
        </div>
      </form>
    </div>

    <div class="company-jobs">
      <div class="role-panel-head compact">
        <h3>Your jobs</h3>
      </div>
      <div v-if="companyJobs.length === 0" class="empty-state">No company jobs yet.</div>
      <article v-for="job in companyJobs" :key="job.id" class="action-card">
        <div>
          <h3>{{ job.title }}</h3>
          <p>{{ job.description }}</p>
          <div class="job-meta">
            <span>{{ job.location }}</span>
            <span>{{ job.jobType.replace('_', ' ') }}</span>
            <span>{{ job.status }}</span>
          </div>
        </div>
        <div class="action-row">
          <button class="ghost-button" type="button" @click="$emit('editJob', job)">Edit</button>
          <button class="danger-button" type="button" :disabled="loading" @click="$emit('deleteJob', job.id)">
            Delete
          </button>
        </div>
      </article>
    </div>

    <div class="company-jobs">
      <div class="role-panel-head compact">
        <h3>Candidate applications</h3>
        <button class="ghost-button small" type="button" :disabled="loading" @click="$emit('refreshApplications')">
          Refresh
        </button>
      </div>
      <div v-if="applications.length === 0" class="empty-state">No candidate applications yet.</div>
      <article v-for="application in applications" :key="application.id" class="action-card">
        <div>
          <span class="company-name">{{ application.candidateName }}</span>
          <h3>{{ application.jobTitle }}</h3>
          <p>{{ application.coverLetter }}</p>
          <div class="job-meta">
            <span>{{ application.candidateEmail }}</span>
            <span>{{ application.cvUrl || 'No CV URL' }}</span>
            <span>{{ application.status }}</span>
          </div>
        </div>
        <div class="action-row">
          <button
            class="primary-button"
            type="button"
            :disabled="loading || application.status === 'ACCEPTED'"
            @click="$emit('reviewApplication', { id: application.id, status: 'ACCEPTED' })"
          >
            Accept
          </button>
          <button
            class="danger-button"
            type="button"
            :disabled="loading || application.status === 'REJECTED'"
            @click="$emit('reviewApplication', { id: application.id, status: 'REJECTED' })"
          >
            Reject
          </button>
        </div>
      </article>
    </div>
  </section>
</template>
