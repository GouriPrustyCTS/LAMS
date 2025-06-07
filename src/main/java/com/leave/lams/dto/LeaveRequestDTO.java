package com.leave.lams.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Leave Request DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {

	private Long leaveRequestId;

	@NotNull(message = "Start date is required")
	private LocalDate startDate;

	@NotNull(message = "End date is required")
	private LocalDate endDate;
	
	@NotNull(message = "Request date is required")
	private LocalDateTime requestDate;

	@NotBlank(message = "Reason is required")
	private String reason;

	@NotBlank(message = "Leave type is required")
	private String leaveType;

	@NotBlank(message = "Status is required")
	private String status;

	@NotNull(message = "Employee ID is required")
	private Long employeeId;
	
	private String name;
}
