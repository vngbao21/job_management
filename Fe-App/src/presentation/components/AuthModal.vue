<script setup lang="ts">
import type { AuthMode } from '../../application/useRecruitmentPage'
import type { RegisterPayload } from '../../domain/entities/user'

defineProps<{
  authForm: RegisterPayload
  authLoading: boolean
  authMessage: string
  authMessageType: 'success' | 'error' | 'info'
  authMode: AuthMode
}>()

defineEmits<{
  close: []
  modeChange: [mode: AuthMode]
  submit: []
}>()
</script>

<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <form class="auth-modal" @submit.prevent="$emit('submit')">
      <div class="modal-head">
        <div>
          <p class="eyebrow">{{ authMode }}</p>
          <h2>{{ authMode === 'login' ? 'Sign in to apply' : 'Create candidate account' }}</h2>
        </div>
        <button class="icon-button" type="button" aria-label="Close" @click="$emit('close')">x</button>
      </div>

      <label v-if="authMode === 'register'">
        Full name
        <input v-model="authForm.fullName" required />
      </label>
      <label>
        Email
        <input v-model="authForm.email" required type="email" />
      </label>
      <label>
        Password
        <input v-model="authForm.password" required minlength="6" type="password" />
      </label>
      <label v-if="authMode === 'register'">
        Phone
        <input v-model="authForm.phone" />
      </label>
      <label v-if="authMode === 'register'">
        Role
        <select v-model="authForm.role">
          <option value="CANDIDATE">Candidate</option>
          <option value="COMPANY">Company</option>
          <option value="ADMIN">Admin</option>
        </select>
      </label>

      <p v-if="authMessage" class="form-message" :class="`form-message-${authMessageType}`">
        {{ authMessage }}
      </p>
      <button class="primary-button" type="submit" :disabled="authLoading">
        {{ authLoading ? 'Please wait...' : authMode === 'login' ? 'Sign in' : 'Create account' }}
      </button>
      <button
        class="text-button"
        type="button"
        @click="$emit('modeChange', authMode === 'login' ? 'register' : 'login')"
      >
        {{ authMode === 'login' ? 'Need an account?' : 'Already have an account?' }}
      </button>
    </form>
  </div>
</template>
