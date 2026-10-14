package com.smartcareer.controller;

import com.smartcareer.entity.Job;
import com.smartcareer.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/company/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public String jobs(Model model, Principal principal) {

        String companyEmail = principal.getName();

        model.addAttribute(
                "jobs",
                jobService.getJobsByCompany(companyEmail)
        );

        return "company/jobs";
    }

    @GetMapping("/new")
    public String showJobForm(Model model) {

        model.addAttribute("job", new Job());

        return "company/job-form";
    }

    @PostMapping("/save")
    public String saveJob(
            @ModelAttribute("job") Job job,
            Principal principal) {

    String companyEmail = principal.getName();


    System.out.println("========== SAVE JOB ==========");
    System.out.println("Logged in company: " + principal.getName());
    System.out.println("Job title: " + job.getJobTitle());
    System.out.println("Location: " + job.getLocation());


       job.setCompanyEmail(companyEmail);

        if (job.getStatus() == null || job.getStatus().isBlank()) {
            job.setStatus("OPEN");
        }

        jobService.saveJob(job);

        System.out.println("========== JOB SAVED ==========");

        return "redirect:/company/jobs";
    }

    @GetMapping("/view/{id}")
    public String viewJob(
            @PathVariable Long id,
            Model model) {

        Job job = jobService.getJobById(id);

        model.addAttribute("job", job);

        return "company/job-details";
    }

    @GetMapping("/edit/{id}")
    public String editJob(
            @PathVariable Long id,
            Model model) {

        Job job = jobService.getJobById(id);

        model.addAttribute("job", job);

        return "company/job-form";
    }

    @PostMapping("/update/{id}")
    public String updateJob(
            @PathVariable Long id,
            @ModelAttribute("job") Job job) {

        jobService.updateJob(id, job);

        return "redirect:/company/jobs";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {

        jobService.deleteJob(id);

        return "redirect:/company/jobs";
    }
}
