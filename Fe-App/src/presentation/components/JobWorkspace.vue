<script setup lang="ts">
import type { Job } from '../../domain/entities/job'
import JobDetail from './JobDetail.vue'
import JobList from './JobList.vue'

defineProps<{
  filteredJobs: Job[]
  salaryText: string
  selectedJob: Job | null
  selectedJobId: number
}>()

defineEmits<{
  selectJob: [id: number]
}>()
</script>

<template>
  <section class="workspace">
    <JobList :jobs="filteredJobs" :selected-job-id="selectedJobId" @select-job="$emit('selectJob', $event)" />
    <JobDetail v-if="selectedJob" :job="selectedJob" :salary-text="salaryText" />
    <div v-else class="job-detail empty-state">No job selected.</div>
  </section>
</template>
