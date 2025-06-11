package com.leave.lams.dto;

public class AuthResponse {
	private String token;
	private Long employeeId;

	public AuthResponse(String token, Long employeeId) {
		this.token = token;
		this.employeeId = employeeId;
	}

	public String getToken() {
		return token;
	}

	public Long getEmployeeId() {
		return employeeId;
	}
}
