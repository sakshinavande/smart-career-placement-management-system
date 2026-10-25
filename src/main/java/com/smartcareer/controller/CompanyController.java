
package com.smartcareer.controller;

import com.smartcareer.entity.Application;
import com.smartcareer.entity.Company;
import com.smartcareer.entity.Job;
import com.smartcareer.entity.User;
import com.smartcareer.service.ApplicationService;
import com.smartcareer.service.CompanyService;
import com.smartcareer.service.JobService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    public CompanyController(
            CompanyService companyService,
            UserService userService,
            JobService jobService,
            ApplicationService applicationService) {

        this.companyService = companyService;
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    // =========================================================
    // COMPANY DASHBOARD WITH PLACEMENT STATISTICS
    // =========================================================

    @GetMapping("/company/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        // Get the currently logged-in user
        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Get jobs posted by this company only
        List<Job> companyJobs =
                jobService.getJobsByCompany(user.getEmail());

        // Collect applications for this company's jobs only
        List<Application> companyApplications = new ArrayList<>();

        for (Job job : companyJobs) {
            companyApplications.addAll(
                    applicationService.getApplicationsByJob(job.getId()));
        }

        // Calculate total jobs and applications
        long totalJobs = companyJobs.size();
        long totalApplications = companyApplications.size();

        // Count shortlisted applications
        long shortlistedCount = companyApplications.stream()
                .filter(app -> app.getStatus() != null
                        && "SHORTLISTED".equalsIgnoreCase(
                                app.getStatus()))
                .count();

        // Count selected applications
        long selectedCount = companyApplications.stream()
                .filter(app -> app.getStatus() != null
                        && "SELECTED".equalsIgnoreCase(
                                app.getStatus()))
                .count();

        // Pass statistics to the Thymeleaf dashboard
        model.addAttribute("totalJobs", totalJobs);
        model.addAttribute("totalApplications", totalApplications);
        model.addAttribute("shortlistedCount", shortlistedCount);
        model.addAttribute("selectedCount", selectedCount);

        return "company/dashboard";
    }

    // =========================================================
    // VIEW COMPANY PROFILE
    // =========================================================

    @GetMapping("/company/profile")
    public String profile(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Company company = companyService
                .getCompanyByUserId(user.getId())
                .orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("company", company);

        return "company/profile";
    }

    // =========================================================
    // CREATE COMPANY PROFILE - SHOW FORM
    // =========================================================

    @GetMapping("/company/profile/create")
    public String createProfile(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Check whether this company already has a profile
        Company existingCompany = companyService
                .getCompanyByUserId(user.getId())
                .orElse(null);

        if (existingCompany != null) {
            return "redirect:/company/profile";
        }

        Company company = new Company();

        model.addAttribute("company", company);

        return "company/profile-form";
    }

    // =========================================================
    // CREATE COMPANY PROFILE - SAVE
    // =========================================================

    @PostMapping("/company/profile/create")
    public String saveProfile(
            Company company,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Connect the profile with the logged-in user
        company.setUser(user);

        companyService.saveCompany(company);

        return "redirect:/company/profile";
    }

    // =========================================================
    // EDIT COMPANY PROFILE - SHOW FORM
    // =========================================================

    @GetMapping("/company/profile/edit/{id}")
    public String editProfile(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Company company = companyService
                .getCompanyById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        // Security check: a company can edit only its own profile
        if (company.getUser() == null
                || company.getUser().getId() == null
                || !company.getUser().getId()
                        .equals(user.getId())) {

            return "redirect:/company/profile";
        }

        model.addAttribute("company", company);

        return "company/profile-form";
    }

    // =========================================================
    // EDIT COMPANY PROFILE - UPDATE
    // =========================================================

    @PostMapping("/company/profile/edit/{id}")
    public String updateProfile(
            @PathVariable Long id,
            Company updatedCompany,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Company existingCompany = companyService
                .getCompanyById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        // Security check: only the profile owner can update it
        if (existingCompany.getUser() == null
                || existingCompany.getUser().getId() == null
                || !existingCompany.getUser().getId()
                        .equals(user.getId())) {

            return "redirect:/company/profile";
        }

        companyService.updateCompany(id, updatedCompany);

        return "redirect:/company/profile";
    }

    // =========================================================
    // DELETE COMPANY PROFILE
    // =========================================================

    @PostMapping("/company/profile/delete/{id}")
    public String deleteProfile(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Company company = companyService
                .getCompanyById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found"));

        // Security check: only the profile owner can delete it
        if (company.getUser() == null
                || company.getUser().getId() == null
                || !company.getUser().getId()
                        .equals(user.getId())) {

            return "redirect:/company/profile";
        }

        companyService.deleteCompany(id);

        return "redirect:/company/profile";
    }
}
