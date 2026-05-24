<script setup lang="ts">
defineProps<{
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}>()

defineEmits<{
  changePage: [page: number]
}>()
</script>

<template>
  <nav class="pagination-bar" aria-label="Job pagination">
    <span>
      Page {{ totalPages === 0 ? 0 : page + 1 }} of {{ totalPages }} · {{ totalElements }} result(s)
    </span>
    <div class="action-row">
      <button class="ghost-button small" type="button" :disabled="page <= 0" @click="$emit('changePage', page - 1)">
        Previous
      </button>
      <button
        class="ghost-button small"
        type="button"
        :disabled="last || totalPages === 0"
        @click="$emit('changePage', page + 1)"
      >
        Next
      </button>
    </div>
  </nav>
</template>
