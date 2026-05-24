<script setup lang="ts">
defineProps<{
  active: boolean
  message: string
}>()
</script>

<template>
  <div v-if="active" class="loading-overlay" role="status" aria-live="polite">
    <div class="loading-panel">
      <span class="loading-spinner" aria-hidden="true"></span>
      <strong>{{ message }}</strong>
    </div>
  </div>
</template>
