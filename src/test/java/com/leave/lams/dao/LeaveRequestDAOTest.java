package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        leaveRequest.setLeaveRequestId(1L);

        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);

        LeaveRequest result = leaveRequestDAO.createLeaveRequest(leaveRequest);	//error
        assertNotNull(result);
        assertEquals(1L, result.getLeaveRequestId());
    }

    @Test
    public void testGetAllLeaveRequests() {
        List<LeaveRequest> mockList = Arrays.asList(new LeaveRequest(), new LeaveRequest());
        when(leaveRequestRepository.findAll()).thenReturn(mockList);

        List<LeaveRequest> result = leaveRequestDAO.getAllLeaveRequests();//error
        assertEquals(2, result.size());
    }

    @Test
    public void testGetLeaveRequestById() {
        LeaveRequest mockRequest = new LeaveRequest();
        mockRequest.setLeaveRequestId(1L);

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));

        Optional<LeaveRequest> result = leaveRequestDAO.getLeaveRequestById(1L);//error
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getLeaveRequestId());
    }

    @Test
    public void testGetLeaveRequestById_NotFound() {
        when(leaveRequestRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<LeaveRequest> result = leaveRequestDAO.getLeaveRequestById(999L);//error
        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateLeaveRequest() {
        LeaveRequest existing = new LeaveRequest();
        existing.setLeaveRequestId(1L);
        Employee emp = new Employee();
        emp.setEmployeeId(1L);
        existing.setEmployee(emp);

        LeaveRequest updated = new LeaveRequest();
        updated.setLeaveRequestId(1L);
        updated.setEmployee(emp);

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(leaveRequestRepository.save(updated)).thenReturn(updated);

        LeaveRequest result = leaveRequestDAO.updateLeaveRequest(1L, updated);//error
        assertNotNull(result);
        assertEquals(1L, result.getLeaveRequestId());
    }

    @Test
    public void testUpdateLeaveRequest_MismatchedEmployeeId() {
        LeaveRequest existing = new LeaveRequest();
        Employee emp1 = new Employee();
        emp1.setEmployeeId(1L);
        existing.setEmployee(emp1);

        LeaveRequest updated = new LeaveRequest();
        Employee emp2 = new Employee();
        emp2.setEmployeeId(2L);
        updated.setEmployee(emp2);

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(existing));

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            leaveRequestDAO.updateLeaveRequest(1L, updated)//error
        );

        assertEquals("Employee ID does not match the owner of this record.", ex.getMessage());
    }

    @Test
    public void testDeleteLeaveRequest_Success() {
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
        verify(leaveRequestRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testUpdateLeaveStatus() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveRequestId(1L);
        leaveRequest.setStatus("PENDING");

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        leaveRequest.setStatus("APPROVED");
        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);

        LeaveRequest updated = leaveRequestDAO.createLeaveRequest(leaveRequest);//error
        assertEquals("APPROVED", updated.getStatus());
    }
}


