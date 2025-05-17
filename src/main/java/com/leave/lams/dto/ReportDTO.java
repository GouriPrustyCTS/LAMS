package com.leave.lams.dto;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Report DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

	private Long reportId;

	@NotNull(message = "Date range start is required")
	private Date dateRangeStart;

	@NotNull(message = "Date range end is required")
	private Date dateRangeEnd;

	@NotNull(message = "Generated date is required")
	private LocalDateTime generatedDate;

	@Min(value = 0, message = "Total attendance cannot be negative")
	private Integer totalAttendance;

	@Min(value = 0, message = "Absenteeism cannot be negative")
	private Integer absenteesim;

	@NotNull(message = "Employee ID is required")
	private Long employeeId;
	
	private String name;
}
