<script setup lang="ts">
defineProps<{
  toast: {
    type: 'success' | 'error' | 'info'
    title: string
    message: string
  } | null
}>()

defineEmits<{
  dismiss: []
}>()
</script>

<template>
  <aside v-if="toast" class="toast-host" aria-live="polite">
    <div class="toast-card" :class="`toast-${toast.type}`">
      <span class="toast-dot" aria-hidden="true"></span>
      <div>
        <strong>{{ toast.title }}</strong>
        <p>{{ toast.message }}</p>
      </div>
      <button class="icon-button" type="button" aria-label="Dismiss notification" @click="$emit('dismiss')">
        x
      </button>
    </div>
  </aside>
</template>
