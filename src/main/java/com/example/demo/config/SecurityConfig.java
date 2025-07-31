package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable authentication - allow all requests
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            // Disable login form and HTTP Basic authentication
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            
            // Security Headers - Protection against common vulnerabilities
            .headers(headers -> headers
                .frameOptions().deny() // Prevent clickjacking
                .contentTypeOptions().and() // Prevent MIME type sniffing
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true)
                )
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .contentSecurityPolicy("default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdn.tailwindcss.com https://unpkg.com; " +
                    "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdn.tailwindcss.com https://fonts.googleapis.com; " +
                    "font-src 'self' https://cdn.jsdelivr.net https://fonts.googleapis.com https://fonts.gstatic.com; " +
                    "img-src 'self' data:; " +
                    "connect-src 'self'; " +
                    "object-src 'none'; " +
                    "base-uri 'self'; " +
                    "form-action 'self'")
                .and()
                // Add additional security headers
                .addHeaderWriter((request, response) -> {
                    response.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");
                    response.setHeader("X-Permitted-Cross-Domain-Policies", "none");
                    response.setHeader("Cross-Origin-Embedder-Policy", "require-corp");
                    response.setHeader("Cross-Origin-Opener-Policy", "same-origin");
                    response.setHeader("Cross-Origin-Resource-Policy", "same-origin");
                })
            )
            
            // CSRF Protection - Keep enabled for state-changing operations
            .csrf(csrf -> csrf
                // Allow GET requests to pass through without CSRF token
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/mainscreen", "GET"),
                    new AntPathRequestMatcher("/about", "GET"),
                    new AntPathRequestMatcher("/", "GET"),
                    new AntPathRequestMatcher("/static/**", "GET"),
                    new AntPathRequestMatcher("/css/**", "GET"),
                    new AntPathRequestMatcher("/js/**", "GET"),
                    new AntPathRequestMatcher("/images/**", "GET")
                )
            )
            
            // Session Management - Secure session handling
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .and()
                .sessionFixation().migrateSession()
            );
            
        return http.build();
    }
}