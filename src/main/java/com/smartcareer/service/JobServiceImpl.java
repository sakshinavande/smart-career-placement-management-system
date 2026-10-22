
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
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job updateJob(Long id, Job job) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found: " + id));

        existingJob.setJobTitle(job.getJobTitle());
        existingJob.setJobType(job.getJobType());
        existingJob.setLocation(job.getLocation());
        existingJob.setRequiredSkills(job.getRequiredSkills());
        existingJob.setMinCgpa(job.getMinCgpa());
        existingJob.setDeadline(job.getDeadline());

        return jobRepository.save(existingJob);
    }
}
