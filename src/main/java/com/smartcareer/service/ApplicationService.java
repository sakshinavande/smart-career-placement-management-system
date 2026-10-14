package com.smartcareer.service;

import com.smartcareer.entity.Application;
import com.smartcareer.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application saveApplication(Application application) {

        if (application.getStudent() == null) {
            throw new RuntimeException("Application must have a student");
        }

        if (application.getJob() == null) {
            throw new RuntimeException("Application must have a job");
        }

        Long studentId = application.getStudent().getId();
        Long jobId = application.getJob().getId();

        if (studentId != null && jobId != null
                && applicationRepository.existsByStudentIdAndJobId(studentId, jobId)) {
            throw new RuntimeException("Application already submitted for this job");
        }

        if (application.getStatus() == null || application.getStatus().isBlank()) {
            application.setStatus("APPLIED");
        }

        if (application.getAppliedAt() == null) {
            application.setAppliedAt(LocalDateTime.now());
        }

        return applicationRepository.save(application);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {

        Optional<Application> application = applicationRepository.findById(id);

        if (application.isEmpty()) {
            throw new RuntimeException("Application not found with ID: " + id);
        }

        return application.get();
    }

    public List<Application> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    public Application updateApplicationStatus(Long id, String status) {

        Application application = getApplicationById(id);

        application.setStatus(status);

        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {

        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Application not found with ID: " + id);
        }

        applicationRepository.deleteById(id);
    }
}
