package com.leave.lams.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;

public class LeaveBalanceDTOTest {

    @Test
    public void testLeaveBalanceDTOCreation() {
        // Arrange
        Long leaveBalanceId = 1L;
        String leaveType = "Sick Leave";
        double balance = 10.0;
        Long employeeId = 101L;

        // Act
        LeaveBalanceDTO leaveBalanceDTO = new LeaveBalanceDTO();
        leaveBalanceDTO.setLeaveBalanceId(leaveBalanceId);
        leaveBalanceDTO.setLeaveType(leaveType);
        leaveBalanceDTO.setBalance(balance);
        leaveBalanceDTO.setEmployeeId(employeeId);

        // Assert
        assertEquals(leaveBalanceId, leaveBalanceDTO.getLeaveBalanceId());
        assertEquals(leaveType, leaveBalanceDTO.getLeaveType());
        assertEquals(balance, leaveBalanceDTO.getBalance());
        assertEquals(employeeId, leaveBalanceDTO.getEmployeeId());
    }

    @Test
    public void testLeaveBalanceDTOFromModel() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId(101L);

        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setLeaveBalanceId(1L);
        leaveBalance.setLeaveType("Sick Leave");
        leaveBalance.setBalance(10.0);
        leaveBalance.setEmployee(employee);

        // Act
        LeaveBalanceDTO leaveBalanceDTO = new LeaveBalanceDTO();

        // Assert
        assertNotNull(leaveBalanceDTO);
        assertEquals(leaveBalance.getLeaveBalanceId(), leaveBalanceDTO.getLeaveBalanceId());
        assertEquals(leaveBalance.getLeaveType(), leaveBalanceDTO.getLeaveType());
        assertEquals(leaveBalance.getBalance(), leaveBalanceDTO.getBalance());
        assertEquals(leaveBalance.getEmployee().getEmployeeId(), leaveBalanceDTO.getEmployeeId());
    }
}