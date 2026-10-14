package com.smartcareer.controller;

import com.smartcareer.dto.EligibilityResult;
import com.smartcareer.entity.Student;
import com.smartcareer.entity.User;
import com.smartcareer.repository.StudentRepository;
import com.smartcareer.service.EligibilityService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EligibilityController {

    private final EligibilityService eligibilityService;
    private final UserService userService;
    private final StudentRepository studentRepository;

    public EligibilityController(
            EligibilityService eligibilityService,
            UserService userService,
            StudentRepository studentRepository) {

        this.eligibilityService = eligibilityService;
        this.userService = userService;
        this.studentRepository = studentRepository;
    }


    @GetMapping("/student/eligibility/{jobId}")
    public String checkEligibility(
            @PathVariable Long jobId,
            Authentication authentication,
            Model model) {

        /*
         * Get currently logged-in student's email
         */
        String email = authentication.getName();


        /*
         * Find User
         */
        User user = userService.getUserByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        /*
         * Find Student Profile
         */
        Student student = studentRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student profile not found. Please create your profile first."
                        ));


        /*
         * Check eligibility
         */
        EligibilityResult result =
                eligibilityService.checkEligibility(
                        jobId,
                        student.getId()
                );


        /*
         * Send data to Thymeleaf page
         */
        model.addAttribute("result", result);
        model.addAttribute("jobId", jobId);

        return "student/eligibility";
    }
}