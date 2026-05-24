import type { Job } from '../../domain/entities/job'

export const demoJobs: Job[] = [
  {
    id: 101,
    companyName: 'OrbitWorks',
    title: 'Java Spring Boot Developer',
    description:
      'Build secure REST APIs for a recruitment platform, optimize database queries, and ship clean backend features with product-minded teammates.',
    requirement:
      'Java 17+, Spring Boot, Spring Security, JPA/Hibernate, MySQL, REST API design, Git workflow, and solid debugging skills.',
    salaryMin: 1800,
    salaryMax: 2800,
    location: 'Ho Chi Minh',
    jobType: 'FULL_TIME',
    status: 'APPROVED',
    tags: ['Spring Boot', 'JWT', 'MySQL'],
    highlight: 'Own auth, company jobs, and candidate application APIs.',
    postedAt: '2 days ago',
  },
  {
    id: 102,
    companyName: 'NovaHire',
    title: 'Fullstack Developer Intern',
    description:
      'Join a compact product squad to connect Vue/React screens with Spring Boot services and improve the candidate application experience.',
    requirement:
      'TypeScript basics, Vue or React, API integration, clean component thinking, and willingness to learn production workflows.',
    salaryMin: 500,
    salaryMax: 900,
    location: 'Da Nang',
    jobType: 'INTERNSHIP',
    status: 'APPROVED',
    tags: ['Vue 3', 'TypeScript', 'REST'],
    highlight: 'Great for portfolio growth with real fullstack tasks.',
    postedAt: 'Today',
  },
  {
    id: 103,
    companyName: 'CloudGate',
    title: 'Remote Backend Engineer',
    description:
      'Develop internal hiring services, admin approval flows, application review tools, and background jobs for notification pipelines.',
    requirement:
      'Spring Boot, PostgreSQL/MySQL, security fundamentals, testing discipline, and experience reading business rules from specs.',
    salaryMin: 2200,
    salaryMax: 3600,
    location: 'Remote',
    jobType: 'REMOTE',
    status: 'APPROVED',
    tags: ['Remote', 'Security', 'Docker'],
    highlight: 'Remote-first backend role with ownership over approval workflows.',
    postedAt: '5 days ago',
  },
]
