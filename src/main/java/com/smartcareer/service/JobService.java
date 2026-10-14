package com.smartcareer.service;



import com.smartcareer.entity.Job;

import java.util.List;

public interface JobService {

    Job saveJob(Job job);

    List<Job> getAllJobs();

    List<Job> getJobsByCompany(String companyEmail);

    Job getJobById(Long id);

    void deleteJob(Long id);

    Job updateJob(Long id, Job job);
}