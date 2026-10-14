package com.smartcareer.repository;

import com.smartcareer.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationId(Long applicationId);

    List<Interview> findByInterviewDate(LocalDate interviewDate);

    List<Interview> findByStatus(String status);
}
