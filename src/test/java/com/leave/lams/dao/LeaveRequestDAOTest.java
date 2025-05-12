package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveRequestRepository;

public class LeaveRequestDAOTest {

    @InjectMocks
    private LeaveRequestDAO leaveRequestDAO;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);

        LeaveRequest result = leaveRequestDAO.createLeaveRequest(leaveRequest);
        assertEquals(leaveRequest, result);
    }

    @Test
    public void testGetAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = Arrays.asList(new LeaveRequest(), new LeaveRequest());
        when(leaveRequestRepository.findAll()).thenReturn(leaveRequests);

        List<LeaveRequest> result = leaveRequestDAO.getAllLeaveRequests();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetLeaveRequestById() {
        LeaveRequest leaveRequest = new LeaveRequest();
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        Optional<LeaveRequest> result = leaveRequestDAO.getLeaveRequestById(1L);
        assertTrue(result.isPresent());
        assertEquals(leaveRequest, result.get());
    }

    @Test
    public void testUpdateLeaveRequest() {
        LeaveRequest existingLeaveRequest = new LeaveRequest();
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        existingLeaveRequest.setEmployee(employee);

        LeaveRequest updatedLeaveRequest = new LeaveRequest();
        updatedLeaveRequest.setEmployee(employee);
        updatedLeaveRequest.setLeaveRequestId(1L);

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(existingLeaveRequest));
        when(leaveRequestRepository.save(existingLeaveRequest)).thenReturn(existingLeaveRequest);

        LeaveRequest result = leaveRequestDAO.updateLeaveRequest(1L, updatedLeaveRequest);
        assertEquals(existingLeaveRequest, result);
    }

    @Test
    public void testDeleteLeaveRequest() {
        when(leaveRequestRepository.existsById(1L)).thenReturn(true);
        doNothing().when(leaveRequestRepository).deleteById(1L);

        boolean result = leaveRequestDAO.deleteLeaveRequest(1L);
        assertTrue(result);
        verify(leaveRequestRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteLeaveRequest_NotFound() {
        when(leaveRequestRepository.existsById(1L)).thenReturn(false);

        boolean result = leaveRequestDAO.deleteLeaveRequest(1L);
        assertFalse(result);
        verify(leaveRequestRepository, never()).deleteById(1L);
    }
    
    @Test
    public void testUpdateLeaveRequest_MismatchedEmployeeId() {
        LeaveRequest existing = new LeaveRequest();
        Employee emp1 = new Employee();
        emp1.setEmployeeId(1L);
        existing.setEmployee(emp1);

        LeaveRequest updated = new LeaveRequest();
        Employee emp2 = new Employee();
        emp2.setEmployeeId(2L); // Mismatch
        updated.setEmployee(emp2);

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(existing));

        try {
            leaveRequestDAO.updateLeaveRequest(1L, updated);
        } catch (IllegalArgumentException e) {
            assertEquals("Employee ID does not match the owner of this record.", e.getMessage());
        }
    }

}
