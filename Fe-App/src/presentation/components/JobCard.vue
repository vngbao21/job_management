<script setup lang="ts">
import type { Job } from '../../domain/entities/job'

defineProps<{
  job: Job
  active: boolean
}>()

defineEmits<{
  select: [id: number]
}>()
</script>

<template>
  <article class="job-card" :class="{ active }" @click="$emit('select', job.id)">
    <div class="job-card-head">
      <span>{{ job.companyName }}</span>
      <small>{{ job.postedAt }}</small>
    </div>
    <h2>{{ job.title }}</h2>
    <p>{{ job.highlight }}</p>
    <div class="job-meta">
      <span>{{ job.location }}</span>
      <span>{{ job.jobType.replace('_', ' ') }}</span>
      <span>{{ job.status }}</span>
    </div>
  </article>
</template>
