package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Security audit configuration for logging security-related events
 */
@Configuration
public class SecurityAuditConfig implements WebMvcConfigurer {

    @Bean
    public SecurityAuditInterceptor securityAuditInterceptor() {
        return new SecurityAuditInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityAuditInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/static/**");
    }

    /**
     * Security audit interceptor that logs security-relevant events
     */
    public static class SecurityAuditInterceptor implements HandlerInterceptor {
        
        private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY_AUDIT");
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // Log security-relevant requests
            if (isSecurityRelevantRequest(request)) {
                logSecurityEvent(request, "REQUEST_RECEIVED");
            }
            
            // Check for suspicious patterns
            if (containsSuspiciousPatterns(request)) {
                logSecurityEvent(request, "SUSPICIOUS_REQUEST_DETECTED");
            }
            
            return true;
        }
        
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            // Log failed requests (potential attacks)
            if (response.getStatus() >= 400) {
                logSecurityEvent(request, "REQUEST_FAILED", "Status: " + response.getStatus());
            }
            
            // Log rate limiting events
            if (response.getStatus() == 429) {
                logSecurityEvent(request, "RATE_LIMIT_EXCEEDED");
            }
        }
        
        /**
         * Check if the request is security-relevant (data modification, admin functions, etc.)
         */
        private boolean isSecurityRelevantRequest(HttpServletRequest request) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            
            // Log all POST, PUT, DELETE requests
            if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
                return true;
            }
            
            // Log specific GET requests that modify state
            return uri.contains("/delete") || uri.contains("/remove") || uri.contains("/buyNow");
        }
        
        /**
         * Check for suspicious patterns that might indicate an attack
         */
        private boolean containsSuspiciousPatterns(HttpServletRequest request) {
            String uri = request.getRequestURI();
            String queryString = request.getQueryString();
            String userAgent = request.getHeader("User-Agent");
            
            // Check for common attack patterns
            String[] suspiciousPatterns = {
                "<script", "javascript:", "vbscript:", "onload=", "onerror=",
                "../", "..%2f", "%2e%2e%2f",
                "union", "select", "insert", "delete", "drop", "exec",
                "alert(", "confirm(", "prompt(",
                "eval(", "expression("
            };
            
            String requestData = (uri + " " + (queryString != null ? queryString : "")).toLowerCase();
            
            for (String pattern : suspiciousPatterns) {
                if (requestData.contains(pattern.toLowerCase())) {
                    return true;
                }
            }
            
            // Check for suspicious user agents
            if (userAgent != null) {
                String[] suspiciousAgents = {"sqlmap", "nikto", "nessus", "burp", "zap"};
                String lowerAgent = userAgent.toLowerCase();
                for (String agent : suspiciousAgents) {
                    if (lowerAgent.contains(agent)) {
                        return true;
                    }
                }
            }
            
            return false;
        }
        
        /**
         * Log security events with standardized format
         */
        private void logSecurityEvent(HttpServletRequest request, String eventType) {
            logSecurityEvent(request, eventType, null);
        }
        
        private void logSecurityEvent(HttpServletRequest request, String eventType, String additionalInfo) {
            String clientIp = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String timestamp = LocalDateTime.now().format(formatter);
            
            StringBuilder logMessage = new StringBuilder()
                .append("[SECURITY_AUDIT] ")
                .append("Timestamp: ").append(timestamp).append(" | ")
                .append("Event: ").append(eventType).append(" | ")
                .append("IP: ").append(clientIp).append(" | ")
                .append("Method: ").append(request.getMethod()).append(" | ")
                .append("URI: ").append(request.getRequestURI());
            
            if (request.getQueryString() != null) {
                logMessage.append("?").append(request.getQueryString());
            }
            
            if (userAgent != null) {
                logMessage.append(" | User-Agent: ").append(userAgent);
            }
            
            if (additionalInfo != null) {
                logMessage.append(" | Info: ").append(additionalInfo);
            }
            
            securityLogger.warn(logMessage.toString());
        }
        
        /**
         * Get the real client IP address, considering proxy headers
         */
        private String getClientIpAddress(HttpServletRequest request) {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
                return xForwardedFor.split(",")[0].trim();
            }
            
            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
                return xRealIp;
            }
            
            return request.getRemoteAddr();
        }
    }
}