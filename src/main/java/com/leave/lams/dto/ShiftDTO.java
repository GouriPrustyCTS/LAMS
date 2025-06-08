package com.leave.lams.dto;

import java.time.LocalTime;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Shift DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {

	private long shiftId;
	
	@NotBlank(message = "Shift name is required") // Added validation for shiftName
	private String shiftName; // Changed 'name' to 'shiftName'

	@NotNull(message = "Shift date is required")
	private Date shiftDate;

	@NotNull(message = "Shift start time is required")
	private LocalTime shiftStartTime;

	@NotNull(message = "Shift end time is required")
	private LocalTime shiftEndTime;

	@NotNull(message = "Employee ID is required")
	private Long employeeId;
	
	private String name;
}
