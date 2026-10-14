package com.smartcareer.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin/dashboard";
        }

        if ("ROLE_STUDENT".equals(role)) {
            return "redirect:/student/dashboard";
        }

        if ("ROLE_COMPANY".equals(role)) {
            return "redirect:/company/dashboard";
        }

        return "redirect:/login?error";
    }
}