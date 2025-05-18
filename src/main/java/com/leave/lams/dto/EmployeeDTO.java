package com.leave.lams.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Employee DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

	private Long employeeId;

	@NotBlank(message = "Name is mandatory")
	private String name;

	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Department cannot be blank")
	private String department;

	@NotBlank(message = "Job title is required")
	private String jobTitle;

	@NotNull(message = "Hire date is required")
	private Date hireDate;
	
	@NotNull(message = "Password is required")
	private String password;
}
