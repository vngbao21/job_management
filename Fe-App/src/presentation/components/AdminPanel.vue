<script setup lang="ts">
import type { Job } from '../../domain/entities/job'
import type { User } from '../../domain/entities/user'

defineProps<{
  dashboardStats: {
    approvedJobs: number
    pendingJobs: number
    applications: number
    activeUsers: number
  }
  loading: boolean
  managedUsers: User[]
  message: string
  pendingJobs: Job[]
}>()

defineEmits<{
  approve: [id: number]
  reject: [id: number]
  refresh: []
  toggleUserStatus: [id: number]
}>()
</script>

<template>
  <section class="role-panel admin-panel">
    <div class="role-panel-head">
      <div>
        <p class="eyebrow">Admin actions</p>
        <h2>Review pending jobs</h2>
      </div>
      <button class="ghost-button" type="button" :disabled="loading" @click="$emit('refresh')">
        Refresh
      </button>
    </div>

    <p v-if="message" class="form-message">{{ message }}</p>

    <div class="admin-stats">
      <div><strong>{{ dashboardStats.approvedJobs }}</strong><span>Approved jobs</span></div>
      <div><strong>{{ dashboardStats.pendingJobs }}</strong><span>Pending jobs</span></div>
      <div><strong>{{ dashboardStats.applications }}</strong><span>Applications</span></div>
      <div><strong>{{ dashboardStats.activeUsers }}</strong><span>Active users</span></div>
    </div>

    <div v-if="pendingJobs.length === 0" class="empty-state">No pending jobs.</div>

    <article v-for="job in pendingJobs" :key="job.id" class="action-card">
      <div>
        <span class="company-name">{{ job.companyName }}</span>
        <h3>{{ job.title }}</h3>
        <p>{{ job.description }}</p>
        <div class="job-meta">
          <span>{{ job.location }}</span>
          <span>{{ job.jobType.replace('_', ' ') }}</span>
          <span>{{ job.status }}</span>
        </div>
      </div>
      <div class="action-row">
        <button class="primary-button" type="button" :disabled="loading" @click="$emit('approve', job.id)">
          Approve
        </button>
        <button class="danger-button" type="button" :disabled="loading" @click="$emit('reject', job.id)">
          Reject
        </button>
      </div>
    </article>

    <div class="role-panel-head compact">
      <h3>User management</h3>
    </div>
    <article v-for="user in managedUsers" :key="user.id" class="action-card">
      <div>
        <span class="company-name">{{ user.role }}</span>
        <h3>{{ user.fullName }}</h3>
        <p>{{ user.email }}</p>
        <div class="job-meta">
          <span>{{ user.status }}</span>
          <span>{{ user.phone || 'No phone' }}</span>
        </div>
      </div>
      <div class="action-row">
        <button class="ghost-button" type="button" :disabled="loading" @click="$emit('toggleUserStatus', user.id)">
          {{ user.status === 'ACTIVE' ? 'Deactivate' : 'Activate' }}
        </button>
      </div>
    </article>
  </section>
</template>
