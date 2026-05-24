<script setup lang="ts">
import type { JobFilters, JobType } from '../../domain/entities/job'

defineProps<{
  filters: JobFilters
  loading: boolean
}>()

defineEmits<{
  reset: []
  search: []
}>()

const jobTypes: Array<{ label: string; value: 'All' | JobType }> = [
  { label: 'All', value: 'All' },
  { label: 'Full time', value: 'FULL_TIME' },
  { label: 'Remote', value: 'REMOTE' },
  { label: 'Internship', value: 'INTERNSHIP' },
  { label: 'Contract', value: 'CONTRACT' },
  { label: 'Part time', value: 'PART_TIME' },
]
</script>

<template>
  <form id="jobs" class="search-band" aria-label="Job search" @submit.prevent="$emit('search')">
    <label>
      Search
      <input v-model="filters.keyword" type="search" placeholder="Java, Spring Boot, Vue..." />
    </label>
    <label>
      Location
      <input v-model="filters.location" type="search" placeholder="Ho Chi Minh, Remote..." />
    </label>
    <label>
      Type
      <select v-model="filters.jobType">
        <option v-for="type in jobTypes" :key="type.value" :value="type.value">
          {{ type.label }}
        </option>
      </select>
    </label>
    <div class="search-actions">
      <button class="primary-button" type="submit" :disabled="loading">Search</button>
      <button class="ghost-button" type="button" :disabled="loading" @click="$emit('reset')">Reset</button>
    </div>
  </form>
</template>
