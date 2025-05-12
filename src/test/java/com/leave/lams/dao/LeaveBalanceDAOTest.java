package com.leave.lams.dao;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;

public class LeaveBalanceDAOTest {

    @InjectMocks
    private LeaveBalanceDAO leaveBalanceDAO;

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLeaveBalance() {
        LeaveBalance leaveBalance = new LeaveBalance();
        when(leaveBalanceRepository.save(leaveBalance)).thenReturn(leaveBalance);

        LeaveBalance result = leaveBalanceDAO.createLeaveBalance(leaveBalance);
        assertEquals(leaveBalance, result);
    }

    @Test
    public void testGetAllLeaveBalances() {
        List<LeaveBalance> leaveBalances = Arrays.asList(new LeaveBalance(), new LeaveBalance());
        when(leaveBalanceRepository.findAll()).thenReturn(leaveBalances);

        List<LeaveBalance> result = leaveBalanceDAO.getAllLeaveBalances();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetLeaveBalanceById() {
        LeaveBalance leaveBalance = new LeaveBalance();
        when(leaveBalanceRepository.findById(1L)).thenReturn(Optional.of(leaveBalance));

        Optional<LeaveBalance> result = leaveBalanceDAO.getLeaveBalanceById(1L);
        assertTrue(result.isPresent());
        assertEquals(leaveBalance, result.get());
    }

    @Test
    public void testUpdateLeaveBalance() {
        LeaveBalance existingLeaveBalance = new LeaveBalance();
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        existingLeaveBalance.setEmployee(employee);

        LeaveBalance newLeaveBalance = new LeaveBalance();
        newLeaveBalance.setEmployee(employee);
        newLeaveBalance.setLeaveType("Sick Leave");
        newLeaveBalance.setBalance(10.0);

        when(leaveBalanceRepository.findById(1L)).thenReturn(Optional.of(existingLeaveBalance));
        when(leaveBalanceRepository.save(existingLeaveBalance)).thenReturn(existingLeaveBalance);

        LeaveBalance result = leaveBalanceDAO.updateLeaveBalance(1L, newLeaveBalance);
        assertEquals(existingLeaveBalance, result);
        assertEquals(newLeaveBalance.getLeaveType(), result.getLeaveType());
        assertEquals(newLeaveBalance.getBalance(), result.getBalance());
    }

    @Test
    public void testDeleteLeaveBalance() {
        doNothing().when(leaveBalanceRepository).deleteById(1L);

        leaveBalanceDAO.deleteLeaveBalance(1L);
        verify(leaveBalanceRepository, times(1)).deleteById(1L);
    }
}

