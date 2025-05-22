package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leave.lams.dto.LeaveRequestDTO;
import com.leave.lams.mapper.LeaveRequestMapper;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveRequestRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestDAOTest {

    @InjectMocks
    private LeaveRequestDAO leaveRequestService;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private LeaveRequestMapper leaveRequestMapper;

    private LeaveRequestDTO dto1, dto2, dto;
    private LeaveRequest entity1, entity2, entity, existing;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dto1 = new LeaveRequestDTO();
        dto2 = new LeaveRequestDTO();
        dto = new LeaveRequestDTO();
        entity1 = new LeaveRequest();
        entity2 = new LeaveRequest();
        entity = new LeaveRequest();
        existing = new LeaveRequest();

        dto.setLeaveRequestId(1L);
        dto.setStatus("PENDING");
        dto.setLeaveType("Sick Leave");
    }

    @Test
    public void testGetAllLeaveRequests() {
        when(leaveRequestRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(leaveRequestMapper.toDTo(entity1)).thenReturn(dto1);
        when(leaveRequestMapper.toDTo(entity2)).thenReturn(dto2);

        List<LeaveRequestDTO> result = leaveRequestService.getAllLeaveRequests();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetLeaveRequestById() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(leaveRequestMapper.toDTo(entity)).thenReturn(dto);

        Optional<LeaveRequestDTO> result = leaveRequestService.getLeaveRequestById(1L);
        assertEquals(dto, result);
    }

    @Test
    public void testCreateLeaveRequest() {
        when(leaveRequestMapper.toEntity(dto)).thenReturn(entity);
        when(leaveRequestRepository.save(entity)).thenReturn(entity);
        when(leaveRequestMapper.toDTo(entity)).thenReturn(dto);

        LeaveRequestDTO result = leaveRequestService.createLeaveRequest(dto);
        assertEquals(dto, result);
    }

    @Test
    public void testUpdateLeaveRequest() {
        Long leaveRequestId = 1L;
        when(leaveRequestRepository.findById(leaveRequestId)).thenReturn(Optional.of(existing));
        when(leaveRequestMapper.toEntity(dto)).thenReturn(existing);
        when(leaveRequestRepository.save(existing)).thenReturn(existing);
        when(leaveRequestMapper.toDTo(existing)).thenReturn(dto);

        LeaveRequestDTO result = leaveRequestService.updateLeaveRequest(leaveRequestId, dto);
        assertEquals(dto, result);
    }

    @Test
    public void testDeleteLeaveRequest() {
        Long leaveRequestId = 1L;
        when(leaveRequestRepository.existsById(leaveRequestId)).thenReturn(true);
        leaveRequestService.deleteLeaveRequest(leaveRequestId);
        verify(leaveRequestRepository, times(1)).deleteById(leaveRequestId);
    }

    @Test
    public void testUpdateLeaveStatus() {
        entity.setStatus("PENDING");

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(entity));
        
        entity.setStatus("APPROVED");
        when(leaveRequestRepository.save(entity)).thenReturn(entity);
        when(leaveRequestMapper.toDTo(entity)).thenReturn(dto);

        LeaveRequestDTO result = leaveRequestService.updateLeaveStatus(1L, "APPROVED");
        assertEquals("APPROVED", result.getStatus());
    }
}
