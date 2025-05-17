package com.leave.lams.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.model.Report;
import com.leave.lams.model.Shift;

public class EmployeeDTOTest {

    @Test
    public void testEmployeeDTOCreation() {
        // Arrange
        Long employeeId = 1L;
        String name = "John Doe";
        String email = "john.doe@example.com";

        // Act
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employeeId);
        employeeDTO.setName(name);
        employeeDTO.setEmail(email);

        // Assert
        assertEquals(employeeId, employeeDTO.getEmployeeId());
        assertEquals(name, employeeDTO.getName());
        assertEquals(email, employeeDTO.getEmail());
    }

    @Test
    public void testEmployeeDTOFromModel() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setAttendances(Arrays.asList(new Attendance()));
        employee.setLeaveRequests(Arrays.asList(new LeaveRequest()));
        employee.setLeaveBalances(Arrays.asList(new LeaveBalance()));
        employee.setShifts(Arrays.asList(new Shift()));
        employee.setReports(Arrays.asList(new Report()));

        // Act
        EmployeeDTO employeeDTO = new EmployeeDTO();

        // Assert
        assertNotNull(employeeDTO);
        assertEquals(employee.getEmployeeId(), employeeDTO.getEmployeeId());
        assertEquals(employee.getName(), employeeDTO.getName());
        assertEquals(employee.getEmail(), employeeDTO.getEmail());
    }
}