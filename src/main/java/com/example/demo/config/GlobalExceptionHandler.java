package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        // Log the full exception for debugging (but don't expose to user)
        logger.error("Unexpected error occurred at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        // Return generic error message to user
        model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error";
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleSQLException(SQLException ex, HttpServletRequest request, Model model) {
        // Log SQL errors but don't expose database details
        logger.error("Database error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        model.addAttribute("error", "A database error occurred. Please try again later.");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        logger.warn("Page not found: {}", ex.getRequestURL());
        
        model.addAttribute("error", "The requested page was not found.");
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request, Model model) {
        logger.warn("Invalid request at {}: {}", request.getRequestURI(), ex.getMessage());
        
        model.addAttribute("error", "Invalid request. Please check your input and try again.");
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        return "error";
    }
}