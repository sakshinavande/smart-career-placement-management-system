package com.smartcareer.controller;

import com.smartcareer.service.ApplicationService;
import com.smartcareer.service.CompanyService;
import com.smartcareer.service.JobService;
import com.smartcareer.service.PlacementService;
import com.smartcareer.service.StudentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final StudentService studentService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final PlacementService placementService;

    public AdminController(
            StudentService studentService,
            CompanyService companyService,
            JobService jobService,
            ApplicationService applicationService,
            PlacementService placementService) {

        this.studentService = studentService;
        this.companyService = companyService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.placementService = placementService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalStudents",
                studentService.getAllStudents().size()
        );

        model.addAttribute(
                "totalCompanies",
                companyService.getAllCompanies().size()
        );

        model.addAttribute(
                "totalJobs",
                jobService.getAllJobs().size()
        );

        model.addAttribute(
                "totalApplications",
                applicationService.getAllApplications().size()
        );

        model.addAttribute(
                "totalPlacements",
                placementService.getAllPlacements().size()
        );

        return "admin/dashboard";
    }

    @GetMapping("/admin/students")
    public String students(Model model) {

        model.addAttribute(
                "students",
                studentService.getAllStudents()
        );

        return "admin/students";
    }

    @GetMapping("/admin/companies")
    public String companies(Model model) {

        model.addAttribute(
                "companies",
                companyService.getAllCompanies()
        );

        return "admin/companies";
    }

    @GetMapping("/admin/jobs")
    public String jobs(Model model) {

        model.addAttribute(
                "jobs",
                jobService.getAllJobs()
        );

        return "admin/jobs";
    }

    @GetMapping("/admin/applications")
    public String applications(Model model) {

        model.addAttribute(
                "applications",
                applicationService.getAllApplications()
        );

        return "admin/applications";
    }

    @GetMapping("/admin/placements")
    public String placements(Model model) {

        model.addAttribute(
                "placements",
                placementService.getAllPlacements()
        );

        return "admin/placements";
    }
}
