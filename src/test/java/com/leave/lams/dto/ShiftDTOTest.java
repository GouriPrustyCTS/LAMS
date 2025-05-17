package com.leave.lams.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;

public class ShiftDTOTest {

    @Test
    public void testShiftDTOCreation() {
        // Arrange
        Long shiftId = 1L;
        Long employeeId = 101L;
        LocalDate shiftLocalDate = LocalDate.parse("2023-01-01");
        Date shiftDate = Date.from(shiftLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalTime shiftStartTime = LocalTime.parse("09:00");
        LocalTime shiftEndTime = LocalTime.parse("17:00");

        // Act
        ShiftDTO shiftDTO = new ShiftDTO();
        shiftDTO.setShiftId(shiftId);
        shiftDTO.setEmployeeId(employeeId);
        shiftDTO.setShiftDate(shiftDate);
        shiftDTO.setShiftStartTime(shiftStartTime);
        shiftDTO.setShiftEndTime(shiftEndTime);

        // Assert
        assertEquals(shiftId, shiftDTO.getShiftId());
        assertEquals(employeeId, shiftDTO.getEmployeeId());
        assertEquals(shiftDate, shiftDTO.getShiftDate());
        assertEquals(shiftStartTime, shiftDTO.getShiftStartTime());
        assertEquals(shiftEndTime, shiftDTO.getShiftEndTime());
    }

    @Test
    public void testShiftDTOFromModel() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId(101L);

        Shift shift = new Shift();
        shift.setShiftId(1L);
        shift.setEmployee(employee);
        LocalDate shiftLocalDate = LocalDate.parse("2023-01-01");
        Date shiftDate = Date.from(shiftLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        shift.setShiftDate(shiftDate);
        shift.setShiftStartTime(LocalTime.parse("09:00"));
        shift.setShiftEndTime(LocalTime.parse("17:00"));

        // Act
        ShiftDTO shiftDTO = new ShiftDTO();

        // Assert
        assertNotNull(shiftDTO);
        assertEquals(shift.getShiftId(), shiftDTO.getShiftId());
        assertEquals(shift.getEmployee().getEmployeeId(), shiftDTO.getEmployeeId());
        assertEquals(shift.getShiftDate(), shiftDTO.getShiftDate());
        assertEquals(shift.getShiftStartTime(), shiftDTO.getShiftStartTime());
        assertEquals(shift.getShiftEndTime(), shiftDTO.getShiftEndTime());
    }
}