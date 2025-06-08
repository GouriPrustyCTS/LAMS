package com.leave.lams.mapper;

import com.leave.lams.dto.ShiftSwapRequestDTO;
import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.model.ShiftSwapRequest;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.repository.ShiftRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiftSwapRequestMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ShiftRepository shiftRepository;

    public ShiftSwapRequest toEntity(ShiftSwapRequestDTO dto) {
        ShiftSwapRequest entity = modelMapper.map(dto, ShiftSwapRequest.class);
        
        // CRITICAL: Manually set nested entities based on IDs from DTO
        // This is where requesterEmployeeId and requestedShiftId from DTO map to fromEmployee/fromShift entities
        if (dto.getRequesterEmployeeId() != null) {
            entity.setFromEmployee(employeeRepository.findById(dto.getRequesterEmployeeId())
                                .orElseThrow(() -> new RuntimeException("Requester Employee not found for ID: " + dto.getRequesterEmployeeId())));
        } else {
             // Handle case where requesterEmployeeId is null (e.g., if it's set by SecurityContext later)
             // For now, throw an exception or set a default.
             throw new RuntimeException("Requester Employee ID must be provided or derived.");
        }

        if (dto.getRequestedShiftId() != null) {
            entity.setFromShift(shiftRepository.findById(dto.getRequestedShiftId())
                             .orElseThrow(() -> new RuntimeException("Requested Shift (from) not found for ID: " + dto.getRequestedShiftId())));
        } else {
             throw new RuntimeException("Requested Shift ID (current shift) must be provided.");
        }

        if (dto.getTargetEmployeeId() != null) {
            entity.setToEmployee(employeeRepository.findById(dto.getTargetEmployeeId())
                              .orElseThrow(() -> new RuntimeException("Target Employee not found for ID: " + dto.getTargetEmployeeId())));
        } else {
             throw new RuntimeException("Target Employee ID must be provided.");
        }

        if (dto.getTargetShiftId() != null) {
            entity.setToShift(shiftRepository.findById(dto.getTargetShiftId())
                           .orElseThrow(() -> new RuntimeException("Target Shift (to) not found for ID: " + dto.getTargetShiftId())));
        } else {
             throw new RuntimeException("Target Shift ID must be provided.");
        }

        return entity;
    }

    public ShiftSwapRequestDTO toDto(ShiftSwapRequest entity) {
        ShiftSwapRequestDTO dto = modelMapper.map(entity, ShiftSwapRequestDTO.class);
        
        // Manually map IDs from nested entities back to DTO for frontend consumption
        if (entity.getFromEmployee() != null) {
            dto.setRequesterEmployeeId(entity.getFromEmployee().getEmployeeId());
        }
        if (entity.getFromShift() != null) {
            dto.setRequestedShiftId(entity.getFromShift().getShiftId());
        }
        if (entity.getToEmployee() != null) {
            dto.setTargetEmployeeId(entity.getToEmployee().getEmployeeId());
        }
        if (entity.getToShift() != null) {
            dto.setTargetShiftId(entity.getToShift().getShiftId());
        }
        return dto;
    }
}