package com.leave.lams.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.model.ShiftSwapRequest;

public class ShiftSwapRequestDTOTest {

    @Test
    public void testShiftSwapRequestDTOCreation() {
        // Arrange
        String status = "PENDING";
        Long fromShiftId = 101L;
        Long toShiftId = 102L;

        // Act
        ShiftSwapRequestDTO shiftSwapRequestDTO = new ShiftSwapRequestDTO();
        shiftSwapRequestDTO.setStatus(status);
        shiftSwapRequestDTO.setFromShiftId(fromShiftId);
        shiftSwapRequestDTO.setToShiftId(toShiftId);

        // Assert
        assertEquals(status, shiftSwapRequestDTO.getStatus());
        assertEquals(fromShiftId, shiftSwapRequestDTO.getFromShiftId());
        assertEquals(toShiftId, shiftSwapRequestDTO.getToShiftId());
    }

    @Test
    public void testShiftSwapRequestDTOFromModel() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1L);

        Employee employee2 = new Employee();
        employee2.setEmployeeId(2L);

        Shift fromShift = new Shift();
        fromShift.setShiftId(101L);
        fromShift.setEmployee(employee1);

        Shift toShift = new Shift();
        toShift.setShiftId(102L);
        toShift.setEmployee(employee2);

        ShiftSwapRequest shiftSwapRequest = new ShiftSwapRequest();
        shiftSwapRequest.setStatus("PENDING");
        shiftSwapRequest.setFromShift(fromShift);
        shiftSwapRequest.setToShift(toShift);

        // Act
        ShiftSwapRequestDTO shiftSwapRequestDTO = new ShiftSwapRequestDTO();

        // Assert
        assertNotNull(shiftSwapRequestDTO);
        assertEquals(shiftSwapRequest.getStatus(), shiftSwapRequestDTO.getStatus());
        assertEquals(shiftSwapRequest.getFromShift().getShiftId(), shiftSwapRequestDTO.getFromShiftId());
        assertEquals(shiftSwapRequest.getToShift().getShiftId(), shiftSwapRequestDTO.getToShiftId());
    }
}