<script setup lang="ts">
import type { Job } from '../../domain/entities/job'

defineProps<{
  job: Job
  salaryText: string
}>()
</script>

<template>
  <article class="job-detail">
    <div class="detail-head">
      <div>
        <span class="company-name">{{ job.companyName }}</span>
        <h2>{{ job.title }}</h2>
      </div>
      <span class="salary">{{ salaryText }}</span>
    </div>
    <p>{{ job.description }}</p>

    <div class="detail-grid">
      <div>
        <span>Location</span>
        <strong>{{ job.location }}</strong>
      </div>
      <div>
        <span>Type</span>
        <strong>{{ job.jobType.replace('_', ' ') }}</strong>
      </div>
      <div>
        <span>Status</span>
        <strong>{{ job.status }}</strong>
      </div>
    </div>

    <section>
      <h3>Requirements</h3>
      <p>{{ job.requirement }}</p>
    </section>

    <div class="tag-row">
      <span v-for="tag in job.tags" :key="tag">{{ tag }}</span>
    </div>
  </article>
</template>
