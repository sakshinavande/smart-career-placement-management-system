
package com.smartcareer.repository;

import com.smartcareer.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
// import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    // Check whether a student has already applied for a job
    boolean existsByStudent_IdAndJob_Id(Long studentId, Long jobId);

    // Get applications by student
    List<Application> findByStudent_Id(Long studentId);

    // Get applications by job
    List<Application> findByJob_Id(Long jobId);

    // Get applications by status
    List<Application> findByStatus(String status);
}
