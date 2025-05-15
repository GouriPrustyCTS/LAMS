package com.leave.lams.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Attendance DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {

	private Long attendanceId;
	
	@NotNull(message = "Clock-in time is required")
	private LocalDateTime clockInTime;

	@NotNull(message = "Clock-out time is required")
	private LocalDateTime clockOutTime;

	@NotNull(message = "Attendance date is required")
	private LocalDate attendanceDate;

	@NotNull(message = "Employee ID is required")
	private Long employeeId;

	@DecimalMin(value = "0.0", inclusive = false, message = "Work hours must be greater than 0")
	@DecimalMax(value = "24.0", message = "Work hours cannot exceed 24")
	private Double workHours;
}
