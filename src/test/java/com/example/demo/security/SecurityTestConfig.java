package com.example.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Security test configuration and basic security tests
 * These tests help ensure security configurations are working correctly
 */
@SpringBootTest
@AutoConfigureWebMvc
public class SecurityTestConfig {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * Test that security headers are properly set
     */
    @Test
    public void testSecurityHeaders() throws Exception {
        setup();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("X-XSS-Protection", "0"))
                .andExpect(header().exists("Content-Security-Policy"));
    }

    /**
     * Test that CSRF protection is enabled for state-changing operations
     */
    @Test
    public void testCSRFProtection() throws Exception {
        setup();
        // POST without CSRF token should fail
        mockMvc.perform(post("/saveProduct"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test rate limiting functionality
     */
    @Test
    public void testRateLimiting() throws Exception {
        setup();
        // Make multiple requests to test rate limiting
        // Note: This test may need adjustment based on rate limiting configuration
        for (int i = 0; i < 15; i++) {
            mockMvc.perform(get("/"));
        }
        // The 16th request should be rate limited
        mockMvc.perform(get("/"))
                .andExpect(status().isTooManyRequests());
    }

    /**
     * Test input validation for XSS attempts
     */
    @Test
    public void testXSSPrevention() throws Exception {
        setup();
        String xssPayload = "<script>alert('xss')</script>";
        
        mockMvc.perform(get("/showFormForAdd")
                .param("name", xssPayload))
                .andExpect(status().isOk())
                // Ensure the script tag is not present in the response
                .andExpect(content().string(org.hamcrest.Matchers.not(
                    org.hamcrest.Matchers.containsString("<script>"))));
    }

    /**
     * Test SQL injection prevention
     */
    @Test
    public void testSQLInjectionPrevention() throws Exception {
        setup();
        String sqlInjectionPayload = "1' OR '1'='1";
        
        mockMvc.perform(get("/showFormForUpdate")
                .param("productID", sqlInjectionPayload))
                .andExpect(status().isBadRequest()); // Should be rejected by validation
    }

    /**
     * Test that sensitive endpoints require proper validation
     */
    @Test
    public void testSensitiveEndpointValidation() throws Exception {
        setup();
        
        // Test delete operations with invalid IDs
        mockMvc.perform(get("/deleteProduct")
                .param("productID", "invalid"))
                .andExpect(status().isBadRequest());
                
        mockMvc.perform(get("/deletePart")
                .param("partID", "-1"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that error pages don't leak sensitive information
     */
    @Test
    public void testErrorHandling() throws Exception {
        setup();
        
        mockMvc.perform(get("/nonexistent"))
                .andExpect(status().isNotFound())
                // Ensure no stack traces or sensitive info in error response
                .andExpect(content().string(org.hamcrest.Matchers.not(
                    org.hamcrest.Matchers.containsString("Exception"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                    org.hamcrest.Matchers.containsString("at com.example"))));
    }

    /**
     * Test that the application handles malformed requests gracefully
     */
    @Test
    public void testMalformedRequestHandling() throws Exception {
        setup();
        
        // Test with extremely long parameter values
        String longString = "a".repeat(10000);
        mockMvc.perform(get("/showFormForAdd")
                .param("name", longString))
                .andExpect(status().isBadRequest());
    }
}