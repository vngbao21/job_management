<script setup lang="ts">
import type { JobApplication } from '../../domain/entities/application'

defineProps<{
  applications: JobApplication[]
  loading: boolean
  message: string
}>()

defineEmits<{
  refreshApplications: []
}>()
</script>

<template>
  <section class="role-panel candidate-panel">
    <div>
      <p class="eyebrow">Candidate actions</p>
      <h2>Applications and profile</h2>
      <p>
        Browse approved jobs, submit a CV for the selected job, and track every application from
        this dashboard.
      </p>
      <p v-if="message" class="form-message">{{ message }}</p>
    </div>
    <div class="action-row">
      <button class="ghost-button" type="button" :disabled="loading" @click="$emit('refreshApplications')">
        Refresh
      </button>
      <a class="primary-button" href="#apply">Go to CV form</a>
    </div>

    <div class="panel-full">
      <h3>My applications</h3>
      <div v-if="applications.length === 0" class="empty-state">No applications submitted yet.</div>
      <article v-for="application in applications" :key="application.id" class="action-card">
        <div>
          <span class="company-name">{{ application.companyName }}</span>
          <h3>{{ application.jobTitle }}</h3>
          <p>{{ application.coverLetter }}</p>
          <div class="job-meta">
            <span>{{ application.status }}</span>
            <span>{{ application.cvName }}</span>
            <span>{{ new Date(application.createdAt).toLocaleDateString() }}</span>
          </div>
        </div>
      </article>
    </div>
  </section>
</template>
