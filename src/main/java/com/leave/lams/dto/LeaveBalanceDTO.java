package com.leave.lams.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Leave Balance DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDTO {

	private Long leaveBalanceId;
	@NotBlank(message = "Leave type is required")
	private String leaveType;

	@DecimalMin(value = "0.0", message = "Leave balance cannot be negative")
	private Double balance;

	@NotNull(message = "Employee ID is required")
	private Long employeeId;
	
	private String name;
}
