package com.smartcareer.service;

import com.smartcareer.entity.Placement;
import com.smartcareer.repository.PlacementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlacementService {

    private final PlacementRepository placementRepository;

    public PlacementService(PlacementRepository placementRepository) {
        this.placementRepository = placementRepository;
    }

    public Placement savePlacement(Placement placement) {

        if (placement.getStudent() == null) {
            throw new RuntimeException("Placement must have a student");
        }

        if (placement.getCompany() == null) {
            throw new RuntimeException("Placement must have a company");
        }

        if (placement.getJob() == null) {
            throw new RuntimeException("Placement must have a job");
        }

        if (placementRepository.existsByStudentId(placement.getStudent().getId())) {
            throw new RuntimeException("Student already has a placement record");
        }

        if (placement.getPlacementDate() == null) {
            placement.setPlacementDate(LocalDate.now());
        }

        if (placement.getStatus() == null || placement.getStatus().isBlank()) {
            placement.setStatus("SELECTED");
        }

        return placementRepository.save(placement);
    }

    public List<Placement> getAllPlacements() {
        return placementRepository.findAll();
    }

    public Placement getPlacementById(Long id) {

        Optional<Placement> placement = placementRepository.findById(id);

        if (placement.isEmpty()) {
            throw new RuntimeException("Placement not found with ID: " + id);
        }

        return placement.get();
    }

    public Placement getPlacementByStudent(Long studentId) {

        Optional<Placement> placement = placementRepository.findByStudentId(studentId);

        if (placement.isEmpty()) {
            throw new RuntimeException("Placement not found for student ID: " + studentId);
        }

        return placement.get();
    }

    public List<Placement> getPlacementsByCompany(Long companyId) {
        return placementRepository.findByCompanyId(companyId);
    }

    public List<Placement> getPlacementsByJob(Long jobId) {
        return placementRepository.findByJobId(jobId);
    }

    public List<Placement> getPlacementsByStatus(String status) {
        return placementRepository.findByStatus(status);
    }

    public Placement updatePlacementStatus(Long id, String status) {

        Placement placement = getPlacementById(id);

        placement.setStatus(status);

        return placementRepository.save(placement);
    }

    public void deletePlacement(Long id) {

        if (!placementRepository.existsById(id)) {
            throw new RuntimeException("Placement not found with ID: " + id);
        }

        placementRepository.deleteById(id);
    }
}
