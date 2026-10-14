package com.smartcareer.service;

import com.smartcareer.entity.Job;
import com.smartcareer.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job saveJob(Job job) {

        if (job.getStatus() == null || job.getStatus().isBlank()) {
            job.setStatus("OPEN");
        }

        return jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public List<Job> getJobsByCompany(String companyEmail) {
        return jobRepository.findByCompanyEmail(companyEmail);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job updateJob(Long id, Job updatedJob) {

        Job existingJob = getJobById(id);

        existingJob.setJobTitle(updatedJob.getJobTitle());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setRequiredSkills(updatedJob.getRequiredSkills());
        existingJob.setMinCgpa(updatedJob.getMinCgpa());
        existingJob.setExperienceRequired(
                updatedJob.getExperienceRequired()
        );
        existingJob.setJobDescription(updatedJob.getJobDescription());
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setApplicationDeadline(
                updatedJob.getApplicationDeadline()
        );
        existingJob.setStatus(updatedJob.getStatus());

        return jobRepository.save(existingJob);
    }
}
