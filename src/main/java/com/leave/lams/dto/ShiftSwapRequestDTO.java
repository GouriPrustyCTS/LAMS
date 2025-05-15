package com.leave.lams.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Shift Swap Request DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftSwapRequestDTO {

	private Long id;

	@NotNull(message = "From employee ID is required")
	private Long fromEmployeeId;

	@NotNull(message = "To employee ID is required")
	private Long toEmployeeId;

	@NotNull(message = "From shift ID is required")
	private Long fromShiftId;

	@NotNull(message = "To shift ID is required")
	private Long toShiftId;

	@Pattern(regexp = "PENDING|APPROVED|REJECTED", message = "Invalid status")
	private String status;
}
