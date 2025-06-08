package com.leave.lams.dto;

import jakarta.validation.constraints.NotBlank;
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

    private Long id; // Optional for new requests, but useful for updates/retrievals

    @NotNull(message = "Requester Employee ID is required")
    private Long requesterEmployeeId; // This will come from the user's session/context, not form input

    @NotNull(message = "Requested Shift ID (current shift of the requester) is required")
    private Long requestedShiftId; // This is the 'Current Shift ID' the user provides on the form

    @NotNull(message = "Target Employee ID is required")
    private Long targetEmployeeId;   // The employee whose shift the requester wants

    @NotNull(message = "Target Shift ID (shift to acquire) is required")
    private Long targetShiftId;      // The 'Target Shift ID' the user provides on the form

    @NotBlank(message = "Status is required")
    private String status; // e.g., PENDING, APPROVED, REJECTED
	
}
