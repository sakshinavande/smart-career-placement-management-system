package com.smartcareer.repository;

import com.smartcareer.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    List<Application> findByJobId(Long jobId);

    List<Application> findByStatus(String status);

    boolean existsByStudentIdAndJobId(Long studentId, Long jobId);
}
