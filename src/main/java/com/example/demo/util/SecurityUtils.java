package com.example.demo.util;

import org.springframework.web.util.HtmlUtils;
import java.util.regex.Pattern;

public class SecurityUtils {

    // Pattern to detect potentially malicious script tags
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
        "<script[^>]*>.*?</script>", 
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    // Pattern to detect potentially malicious event handlers
    private static final Pattern EVENT_PATTERN = Pattern.compile(
        "on\\w+\\s*=\\s*[\"'][^\"']*[\"']", 
        Pattern.CASE_INSENSITIVE
    );
    
    // Pattern to detect javascript: URLs
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile(
        "javascript:", 
        Pattern.CASE_INSENSITIVE
    );

    /**
     * Sanitizes user input to prevent XSS attacks
     * @param input The user input to sanitize
     * @return Sanitized input safe for display
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // HTML encode the input to prevent XSS
        String sanitized = HtmlUtils.htmlEscape(input);
        
        // Remove any remaining script tags
        sanitized = SCRIPT_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remove event handlers
        sanitized = EVENT_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remove javascript: URLs
        sanitized = JAVASCRIPT_PATTERN.matcher(sanitized).replaceAll("");
        
        return sanitized.trim();
    }

    /**
     * Validates that a string contains only alphanumeric characters and basic punctuation
     * @param input The input to validate
     * @return true if the input is safe, false otherwise
     */
    public static boolean isAlphanumericSafe(String input) {
        if (input == null) {
            return true;
        }
        
        // Allow letters, numbers, spaces, and basic punctuation
        return input.matches("^[a-zA-Z0-9\\s\\-_.,!?()]*$");
    }

    /**
     * Validates that a numeric string is actually numeric
     * @param input The input to validate
     * @return true if the input is a valid number, false otherwise
     */
    public static boolean isValidNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates that an ID parameter is a positive integer
     * @param id The ID to validate
     * @return true if the ID is valid, false otherwise
     */
    public static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        try {
            int idValue = Integer.parseInt(id.trim());
            return idValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Limits the length of input to prevent buffer overflow attacks
     * @param input The input to limit
     * @param maxLength Maximum allowed length
     * @return Truncated input if necessary
     */
    public static String limitLength(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        
        if (input.length() > maxLength) {
            return input.substring(0, maxLength);
        }
        
        return input;
    }
}