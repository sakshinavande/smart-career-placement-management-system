
package com.smartcareer.controller;

import com.smartcareer.entity.Application;
import com.smartcareer.entity.Job;
import com.smartcareer.entity.Student;
import com.smartcareer.entity.User;
import com.smartcareer.service.ApplicationService;
import com.smartcareer.service.JobService;
import com.smartcareer.service.StudentService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    public StudentController(
            StudentService studentService,
            UserService userService,
            JobService jobService,
            ApplicationService applicationService) {

        this.studentService = studentService;
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    // Get the currently logged-in user's student profile
    private Student getLoggedInStudent(Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("Please log in first.");
        }

        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        return studentService.getStudentByUserId(user.getId());
    }

    // STUDENT DASHBOARD
    @GetMapping("/student/dashboard")
    public String dashboard() {
        return "student/dashboard";
    }

    // VIEW STUDENT PROFILE
    @GetMapping("/student/profile")
    public String profile(
            Authentication authentication,
            Model model) {

        Student student = getLoggedInStudent(authentication);

        model.addAttribute("user", student.getUser());
        model.addAttribute("student", student);

        return "student/profile";
    }

    // CREATE STUDENT PROFILE
    @GetMapping("/student/profile/create")
    public String createProfile(Model model) {

        model.addAttribute("student", new Student());

        return "student/profile-form";
    }

    // SAVE STUDENT PROFILE
    @PostMapping("/student/profile/create")
    public String saveProfile(
            Student student,
            Authentication authentication) {

        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        student.setUser(user);
        studentService.saveStudent(student);

        return "redirect:/student/profile";
    }

    // EDIT STUDENT PROFILE
    @GetMapping("/student/profile/edit/{id}")
    public String editProfile(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Student loggedInStudent = getLoggedInStudent(authentication);

        // A student can edit only their own profile
        if (!loggedInStudent.getId().equals(id)) {
            return "redirect:/student/profile";
        }

        model.addAttribute("student", loggedInStudent);

        return "student/profile-form";
    }

    // UPDATE STUDENT PROFILE
    @PostMapping("/student/profile/edit/{id}")
    public String updateProfile(
            @PathVariable Long id,
            Student updatedStudent,
            Authentication authentication) {

        Student loggedInStudent = getLoggedInStudent(authentication);

        // Prevent updating another student's profile
        if (!loggedInStudent.getId().equals(id)) {
            return "redirect:/student/profile";
        }

        updatedStudent.setUser(loggedInStudent.getUser());
        studentService.updateStudent(id, updatedStudent);

        return "redirect:/student/profile";
    }

    // VIEW AVAILABLE JOBS
    @GetMapping("/student/jobs")
    public String studentJobs(Model model) {

        List<Job> openJobs = jobService.getAllJobs()
                .stream()
                .filter(job -> job.getStatus() != null
                        && "OPEN".equalsIgnoreCase(job.getStatus()))
                .collect(Collectors.toList());

        model.addAttribute("jobs", openJobs);

        return "student/jobs";
    }

    // APPLY FOR A JOB
    @PostMapping("/student/jobs/{id}/apply")
    public String applyForJob(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Student student = getLoggedInStudent(authentication);
        Job job = jobService.getJobById(id);

        if (job.getStatus() == null
                || !"OPEN".equalsIgnoreCase(job.getStatus())) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Applications for this job are closed.");

            return "redirect:/student/jobs";
        }

        if (applicationService.hasApplied(student.getId(), job.getId())) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You have already applied for this job.");

            return "redirect:/student/jobs";
        }

        Application application = new Application();
        application.setStudent(student);
        application.setJob(job);
        application.setStatus("APPLIED");

        applicationService.saveApplication(application);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Your job application was submitted successfully.");

        return "redirect:/student/applications";
    }

    // VIEW MY APPLICATIONS
    @GetMapping("/student/applications")
    public String myApplications(
            Authentication authentication,
            Model model) {

        Student student = getLoggedInStudent(authentication);

        model.addAttribute(
                "applications",
                applicationService.getApplicationsByStudent(student.getId()));

        return "student/applications";
    }
}
