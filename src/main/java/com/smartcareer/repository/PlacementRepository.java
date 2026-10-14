package com.smartcareer.repository;

import com.smartcareer.entity.Placement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlacementRepository extends JpaRepository<Placement, Long> {

    Optional<Placement> findByStudentId(Long studentId);

    List<Placement> findByCompanyId(Long companyId);

    List<Placement> findByJobId(Long jobId);

    List<Placement> findByStatus(String status);

    boolean existsByStudentId(Long studentId);
}
