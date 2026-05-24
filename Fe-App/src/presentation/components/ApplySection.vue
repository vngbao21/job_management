<script setup lang="ts">
import type { CandidateApplicationForm } from '../../domain/entities/application'
import type { Job } from '../../domain/entities/job'

defineProps<{
  applicationForm: CandidateApplicationForm
  selectedJob: Job | null
}>()

const emit = defineEmits<{
  cvChange: [fileName: string]
  submitApplication: []
}>()

function handleCvInput(event: Event) {
  const input = event.target as HTMLInputElement
  emit('cvChange', input.files?.[0]?.name || '')
}
</script>

<template>
  <section id="apply" class="apply-section">
    <div class="section-copy">
      <p class="eyebrow">Candidate application</p>
      <h2>Apply CV for the selected job</h2>
      <p>
        Send a cover letter and CV reference to the backend application API. Your application
        history updates after the request succeeds.
      </p>
    </div>

    <form class="apply-form" @submit.prevent="$emit('submitApplication')">
      <label>
        Full name
        <input v-model="applicationForm.fullName" required placeholder="Alex Morgan" />
      </label>
      <label>
        Email
        <input v-model="applicationForm.email" required type="email" placeholder="you@email.com" />
      </label>
      <label>
        Phone
        <input v-model="applicationForm.phone" placeholder="0900000000" />
      </label>
      <label>
        CV file
        <input accept=".pdf,.doc,.docx" required type="file" @change="handleCvInput" />
        <small v-if="applicationForm.cvName">{{ applicationForm.cvName }}</small>
      </label>
      <label class="wide">
        Cover letter
        <textarea v-model="applicationForm.coverLetter" rows="5"></textarea>
      </label>
      <button class="primary-button wide" type="submit" :disabled="!selectedJob">
        {{ selectedJob ? `Apply ${selectedJob.title}` : 'Select a job to apply' }}
      </button>
    </form>
  </section>
</template>
