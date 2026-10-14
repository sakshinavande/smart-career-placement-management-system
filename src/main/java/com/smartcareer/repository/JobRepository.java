package com.smartcareer.repository;

import com.smartcareer.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompanyEmail(String companyEmail);

    List<Job> findByStatus(String status);
}