
package com.smartcareer.controller;

import com.smartcareer.entity.Application;
import com.smartcareer.entity.Job;
import com.smartcareer.entity.User;
import com.smartcareer.service.ApplicationService;
import com.smartcareer.service.JobService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    public ApplicationController(
            ApplicationService applicationService,
            JobService jobService,
            UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

    // Display applications for jobs posted by the logged-in company
    @GetMapping("/company/applications")
    public String companyApplications(
            Authentication authentication,
            Model model) {

        User company = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        List<Job> companyJobs =
                jobService.getJobsByCompany(company.getEmail());

        List<Application> applications = companyJobs.stream()
                .flatMap(job -> applicationService
                        .getApplicationsByJob(job.getId()).stream())
                .toList();

        model.addAttribute("applications", applications);

        return "company/applications";
    }

    // Update an application's status
    @PostMapping("/company/applications/{id}/status")
    public String updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User company = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Application application =
                applicationService.getApplicationById(id);

        // Verify that this application belongs to a job posted by this company
        if (application.getJob() == null
                || !company.getEmail().equalsIgnoreCase(
                        application.getJob().getCompanyEmail())) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You are not authorized to update this application.");
            return "redirect:/company/applications";
        }

        if (!List.of("SHORTLISTED", "REJECTED", "SELECTED")
                .contains(status.toUpperCase())) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Invalid application status.");
            return "redirect:/company/applications";
        }

        applicationService.updateApplicationStatus(
                id, status.toUpperCase());

        redirectAttributes.addFlashAttribute(
                "successMessage", "Application status updated successfully.");

        return "redirect:/company/applications";
    }
}
