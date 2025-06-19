package com.leave.lams.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dto.AuthResponse;
import com.leave.lams.model.AuthRequest;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.EmployeeDetailsService;
import com.leave.lams.service.TokenBlacklistService;
import com.leave.lams.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

//	Disabled for frontend dev - to disable the security
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmployeeDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private TokenBlacklistService blacklistService;

	@Autowired
	EmployeeRepository employeeRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
		logger.info("Request received: POST /login for user: {}", request.getUsername());
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (AuthenticationException ex) {
			logger.warn("Authentication failed for user {}: {}", request.getUsername(), ex.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

		Employee employee = employeeRepository.findByEmail(request.getUsername()).orElse(null);
		long employeeId = 0L;
		if (employee != null) {
			employeeId = employee.getEmployeeId();
		}
		final String token = jwtUtil.generateToken(userDetails, employeeId);

		logger.info("Login successful for user: {}", request.getUsername());

		return ResponseEntity.ok(new AuthResponse(token, employeeId));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			blacklistService.blacklistToken(token);
			logger.info("Token blacklisted: {}", token);
		}
		return ResponseEntity.ok("Logged out successfully. Token invalidated.");
	}

}
