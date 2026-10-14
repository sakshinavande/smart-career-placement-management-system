package com.smartcareer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // ================= CSRF =================
                .csrf(csrf -> csrf.disable())

                // ================= AUTHORIZATION =================
                .authorizeHttpRequests(auth -> auth

                        // Public pages
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // Admin
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // Student
                        .requestMatchers("/student/**")
                        .hasRole("STUDENT")

                        // Company
                        .requestMatchers("/company/**")
                        .hasRole("COMPANY")

                        // All other pages require login
                        .anyRequest()
                        .authenticated()
                )

                // ================= LOGIN =================
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                // ================= LOGOUT =================
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}