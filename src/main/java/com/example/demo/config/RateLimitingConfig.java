package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiting configuration to prevent DoS attacks
 * Implements a simple token bucket algorithm per IP address
 */
@Configuration
public class RateLimitingConfig implements WebMvcConfigurer {

    @Bean
    public RateLimitingInterceptor rateLimitingInterceptor() {
        return new RateLimitingInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/static/**");
    }

    /**
     * Rate limiting interceptor that tracks requests per IP address
     */
    public static class RateLimitingInterceptor implements HandlerInterceptor {
        
        // Maximum requests per minute per IP
        private static final int MAX_REQUESTS_PER_MINUTE = 60;
        
        // Track request counts per IP address
        private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
        
        // Scheduled executor to reset counters
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        public RateLimitingInterceptor() {
            // Reset counters every minute
            scheduler.scheduleAtFixedRate(this::resetCounters, 1, 1, TimeUnit.MINUTES);
        }
        
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String clientIp = getClientIpAddress(request);
            
            // Get or create counter for this IP
            AtomicInteger counter = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
            
            // Check if rate limit exceeded
            if (counter.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
                response.setContentType("application/json");
                response.getWriter().write(
                    "{\"error\": \"Rate limit exceeded. Please try again later.\", \"status\": 429}"
                );
                return false;
            }
            
            return true;
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
        
        /**
         * Reset all request counters
         */
        private void resetCounters() {
            requestCounts.clear();
        }
    }
}