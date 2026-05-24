<script setup lang="ts">
import type { Job } from '../../domain/entities/job'
import JobCard from './JobCard.vue'

defineProps<{
  jobs: Job[]
  selectedJobId: number
}>()

defineEmits<{
  selectJob: [id: number]
}>()
</script>

<template>
  <div class="job-list" aria-label="Job results">
    <JobCard
      v-for="job in jobs"
      :key="job.id"
      :job="job"
      :active="selectedJobId === job.id"
      @select="$emit('selectJob', $event)"
    />
    <p v-if="jobs.length === 0" class="empty-state">No matching jobs.</p>
  </div>
</template>
