package com.smartcareer.controller;

import com.smartcareer.entity.Company;
import com.smartcareer.entity.User;
import com.smartcareer.service.CompanyService;
import com.smartcareer.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    public CompanyController(
            CompanyService companyService,
            UserService userService) {

        this.companyService = companyService;
        this.userService = userService;
    }

    // =========================================================
    // COMPANY DASHBOARD
    // =========================================================
@GetMapping("/company/dashboard")
public String dashboard() {
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

        /*
         * Check whether this company already has
         * a profile.
         */
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

        /*
         * Connect the company profile with
         * the currently logged-in user.
         */
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
                        new RuntimeException(
                                "Company not found"));

        /*
         * Security check:
         * A company can edit only its own profile.
         */
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
                        new RuntimeException(
                                "Company not found"));

        /*
         * Security check:
         * Only the owner of the profile can update it.
         */
        if (existingCompany.getUser() == null
                || existingCompany.getUser().getId() == null
                || !existingCompany.getUser().getId()
                        .equals(user.getId())) {

            return "redirect:/company/profile";
        }

        companyService.updateCompany(
                id,
                updatedCompany
        );

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
                        new RuntimeException(
                                "Company not found"));

        /*
         * Security check:
         * Only the owner can delete the profile.
         */
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