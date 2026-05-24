<script setup lang="ts">
import heroImg from '../../assets/hero.png'

defineProps<{
  jobCount: number
}>()
</script>

<template>
  <section class="hero-section">
    <img class="hero-image" :src="heroImg" alt="Recruitment dashboard preview" />
    <div class="hero-copy">
      <p class="eyebrow">{{ jobCount }} open roles</p>
      <h1>Find the right role faster.</h1>
      <p>
        Search approved jobs, review details, and apply with your CV from one focused workspace.
      </p>
      <div class="hero-actions">
        <a class="primary-button" href="#jobs">Browse jobs</a>
        <a class="secondary-button" href="#apply">Apply selected job</a>
      </div>
    </div>
  </section>
</template>
