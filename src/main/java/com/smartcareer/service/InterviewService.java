package com.smartcareer.service;

import com.smartcareer.entity.Interview;
import com.smartcareer.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public Interview saveInterview(Interview interview) {

        if (interview.getApplication() == null) {
            throw new RuntimeException("Interview must be linked to an application");
        }

        if (interview.getStatus() == null || interview.getStatus().isBlank()) {
            interview.setStatus("SCHEDULED");
        }

        return interviewRepository.save(interview);
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Interview getInterviewById(Long id) {

        Optional<Interview> interview = interviewRepository.findById(id);

        if (interview.isEmpty()) {
            throw new RuntimeException("Interview not found with ID: " + id);
        }

        return interview.get();
    }

    public List<Interview> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    public List<Interview> getInterviewsByDate(LocalDate date) {
        return interviewRepository.findByInterviewDate(date);
    }

    public List<Interview> getInterviewsByStatus(String status) {
        return interviewRepository.findByStatus(status);
    }

    public Interview updateInterview(Long id, Interview updatedInterview) {

        Interview existingInterview = getInterviewById(id);

        existingInterview.setInterviewDate(updatedInterview.getInterviewDate());
        existingInterview.setInterviewTime(updatedInterview.getInterviewTime());
        existingInterview.setMode(updatedInterview.getMode());
        existingInterview.setLocation(updatedInterview.getLocation());
        existingInterview.setStatus(updatedInterview.getStatus());
        existingInterview.setFeedback(updatedInterview.getFeedback());

        return interviewRepository.save(existingInterview);
    }

    public void deleteInterview(Long id) {

        if (!interviewRepository.existsById(id)) {
            throw new RuntimeException("Interview not found with ID: " + id);
        }

        interviewRepository.deleteById(id);
    }
}
