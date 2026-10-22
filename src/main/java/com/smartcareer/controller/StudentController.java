
package com.smartcareer.controller;

import com.smartcareer.entity.Job;
import com.smartcareer.entity.Student;
import com.smartcareer.entity.User;
import com.smartcareer.service.JobService;
import com.smartcareer.service.StudentService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final JobService jobService;

    public StudentController(
            StudentService studentService,
            UserService userService,
            JobService jobService) {

        this.studentService = studentService;
        this.userService = userService;
        this.jobService = jobService;
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

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Student student = studentService
                .getStudentByUserId(user.getId());

        model.addAttribute("user", user);
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

        String email = authentication.getName();

        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        student.setUser(user);
        studentService.saveStudent(student);

        return "redirect:/student/profile";
    }

    // EDIT STUDENT PROFILE
    @GetMapping("/student/profile/edit/{id}")
    public String editProfile(
            @PathVariable Long id,
            Model model) {

        Student student = studentService.getStudentById(id);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        model.addAttribute("student", student);

        return "student/profile-form";
    }

    // UPDATE STUDENT PROFILE
    @PostMapping("/student/profile/edit/{id}")
    public String updateProfile(
            @PathVariable Long id,
            Student updatedStudent) {

        studentService.updateStudent(id, updatedStudent);

        return "redirect:/student/profile";
    }

    // STUDENT JOB LISTINGS
    @GetMapping("/student/jobs")
    public String studentJobs(Model model) {

        List<Job> openJobs = jobService.getAllJobs()
                .stream()
                .filter(job -> job.getStatus() != null
                &&
                        "OPEN".equalsIgnoreCase(job.getStatus()))
                .collect(Collectors.toList());

        
        model.addAttribute("jobs", openJobs);

        return "student/jobs";
    }
}
