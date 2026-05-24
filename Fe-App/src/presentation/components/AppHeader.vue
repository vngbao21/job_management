<script setup lang="ts">
import type { User } from '../../domain/entities/user'

defineProps<{
  apiStatus: string
  currentUser: User | null
}>()

defineEmits<{
  login: []
  logout: []
}>()
</script>

<template>
  <header class="topbar">
    <a class="brand" href="#jobs" aria-label="JobPilot home">
      <span class="brand-mark">JP</span>
      <span>JobPilot</span>
    </a>
    <nav class="nav-links" aria-label="Main navigation">
      <a href="#jobs">Jobs</a>
      <a href="#dashboard">Dashboard</a>
      <a href="#apply">Apply</a>
    </nav>
    <div class="account-actions">
      <div v-if="currentUser" class="account-card" :title="apiStatus">
        <span class="account-avatar">{{ currentUser.fullName.charAt(0) }}</span>
        <span class="account-copy">
          <strong>{{ currentUser.fullName }}</strong>
          <small>{{ currentUser.role.toLowerCase() }}</small>
        </span>
        <button class="ghost-button small" type="button" @click="$emit('logout')">Logout</button>
      </div>
      <button v-else class="primary-button small" type="button" @click="$emit('login')">
        Sign in
      </button>
    </div>
  </header>
</template>
