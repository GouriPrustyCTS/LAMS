package com.leave.lams.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public ResponseEntity<String> loginPrompt() {
    	logger.info("Request received: GET /login");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Please log in via POST /login with credentials.");
    }

    @GetMapping("/success")
    public ResponseEntity<String> loginSuccess() {
    	logger.info("Request received: GET /login-success");
        return ResponseEntity.ok("Login successful!");
    }

    @GetMapping("/logout-success")
    public ResponseEntity<String> logoutSuccess() {
    	logger.info("Request received: GET /logout-success");
        return ResponseEntity.ok("Logout successful!");
    }

    @GetMapping("/login-error")
    public ResponseEntity<String> loginError() {
    	logger.info("Request received: GET /login-error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials. Please try again.");
    }
}






