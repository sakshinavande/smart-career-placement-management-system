package com.smartcareer.controller;

import com.smartcareer.entity.Student;
import com.smartcareer.entity.User;
import com.smartcareer.service.StudentService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    public StudentController(
            StudentService studentService,
            UserService userService) {

        this.studentService = studentService;
        this.userService = userService;
    }

    // =========================
    // STUDENT DASHBOARD
    // =========================

    @GetMapping("/student/dashboard")
    public String dashboard() {
        return "student/dashboard";
    }

    // =========================
    // VIEW STUDENT PROFILE
    // =========================

    // @GetMapping("/student/profile")
    // public String profile(
    //         Authentication authentication,
    //         Model model) {

    //     String email = authentication.getName();

    //     User user = userService.getUserByEmail(email)
    //             .orElseThrow(() ->
    //                     new RuntimeException("User not found"));

    //     Student student = studentService
    //             .getStudentByUserId(user.getId());

    //     model.addAttribute("user", user);
    //     model.addAttribute("student", student);

    //     return "student/profile";
    // }

    // // =========================
    // // CREATE STUDENT PROFILE
    // // =========================

   @GetMapping("/student/profile/create")
public String createProfile(Model model) {
    model.addAttribute("student", new Student());
    return "student/profile-form";
}

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
    // =========================
    // SAVE STUDENT PROFILE
    // =========================

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

    // =========================
    // EDIT STUDENT PROFILE
    // =========================

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

    // =========================
    // UPDATE STUDENT PROFILE
    // =========================

    @PostMapping("/student/profile/edit/{id}")
    public String updateProfile(
            @PathVariable Long id,
            Student updatedStudent) {

        studentService.updateStudent(id, updatedStudent);

        return "redirect:/student/profile";
    }
}