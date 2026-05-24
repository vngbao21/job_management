<script setup lang="ts">
import { useRecruitmentPage } from '../../application/useRecruitmentPage'
import AppHeader from '../components/AppHeader.vue'
import ApplySection from '../components/ApplySection.vue'
import AuthModal from '../components/AuthModal.vue'
import BackendStatusSection from '../components/BackendStatusSection.vue'
import HeroSection from '../components/HeroSection.vue'
import JobSearchBar from '../components/JobSearchBar.vue'
import JobWorkspace from '../components/JobWorkspace.vue'
import JobPagination from '../components/JobPagination.vue'
import LoadingOverlay from '../components/LoadingOverlay.vue'
import RoleDashboard from '../components/RoleDashboard.vue'
import ToastHost from '../components/ToastHost.vue'

const page = useRecruitmentPage()
</script>

<template>
  <main class="app-shell">
    <AppHeader
      :api-status="page.apiStatus.value"
      :current-user="page.currentUser.value"
      @login="page.requestAuth()"
      @logout="page.logout"
    />
    <HeroSection :job-count="page.jobs.value.length" />
    <JobSearchBar
      :filters="page.filters"
      :loading="page.roleActionLoading.value"
      @reset="page.resetJobSearch"
      @search="page.searchJobs"
    />
    <JobWorkspace
      :filtered-jobs="page.filteredJobs.value"
      :salary-text="page.salaryText.value"
      :selected-job="page.selectedJob.value"
      :selected-job-id="page.selectedJobId.value"
      @select-job="page.selectJob"
    />
    <JobPagination
      :last="page.jobPage.last"
      :page="page.jobPage.page"
      :size="page.jobPage.size"
      :total-elements="page.jobPage.totalElements"
      :total-pages="page.jobPage.totalPages"
      @change-page="page.changeJobPage"
    />
    <RoleDashboard
      :admin-pending-jobs="page.adminPendingJobs.value"
      :candidate-applications="page.candidateApplications.value"
      :company-applications="page.companyApplications.value"
      :company-job-form="page.companyJobForm"
      :company-jobs="page.companyJobs.value"
      :company-profile-form="page.companyProfileForm"
      :current-user="page.currentUser.value"
      :dashboard-stats="page.dashboardStats.value"
      :loading="page.roleActionLoading.value"
      :managed-users="page.managedUsers.value"
      :message="page.roleActionMessage.value"
      @approve-job="page.approveJob"
      @delete-company-job="page.deleteCompanyJob"
      @edit-company-job="page.editCompanyJob"
      @refresh-candidate-applications="page.loadCandidateApplications"
      @refresh-company-applications="page.loadCompanyApplications"
      @refresh-role-data="page.loadRoleData"
      @reject-job="page.rejectJob"
      @reset-company-job-form="page.resetCompanyJobForm"
      @review-application="page.reviewApplication"
      @save-company-job="page.saveCompanyJob"
      @save-company-profile="page.saveCompanyProfile"
      @toggle-user-status="page.toggleUserStatus"
    />
    <ApplySection
      :application-form="page.applicationForm"
      :selected-job="page.selectedJob.value"
      @cv-change="page.handleCvChange"
      @submit-application="page.submitApplication"
    />
    <BackendStatusSection />
    <AuthModal
      v-if="page.authOpen.value"
      :auth-form="page.authForm"
      :auth-loading="page.authLoading.value"
      :auth-message="page.authMessage.value"
      :auth-message-type="page.authMessageType.value"
      :auth-mode="page.authMode.value"
      @close="page.authOpen.value = false"
      @mode-change="page.setAuthMode"
      @submit="page.submitAuth"
    />
    <LoadingOverlay :active="page.globalLoading.value" :message="page.globalLoadingText.value" />
    <ToastHost :toast="page.toast.value" @dismiss="page.dismissToast" />
  </main>
</template>
