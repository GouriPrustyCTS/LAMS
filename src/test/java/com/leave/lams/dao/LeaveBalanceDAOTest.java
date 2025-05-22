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

import com.leave.lams.dto.LeaveBalanceDTO;
import com.leave.lams.mapper.LeaveBalanceMapper;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveBalanceDAOTest {

    @InjectMocks
    private LeaveBalanceDAO leaveBalanceService;

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @Mock
    private LeaveBalanceMapper leaveBalanceMapper;

    private LeaveBalanceDTO dto1, dto2, dto;
    private LeaveBalance entity1, entity2, entity, existing;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dto1 = new LeaveBalanceDTO();
        dto2 = new LeaveBalanceDTO();
        dto = new LeaveBalanceDTO();
        entity1 = new LeaveBalance();
        entity2 = new LeaveBalance();
        entity = new LeaveBalance();
        existing = new LeaveBalance();

        dto.setLeaveBalanceId(1L);
        dto.setLeaveType("Sick Leave");
        dto.setBalance(10.0);
    }

    @Test
    public void testGetAllLeaveBalances() {
        when(leaveBalanceRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(leaveBalanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(leaveBalanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<LeaveBalanceDTO> result = leaveBalanceService.getAllLeaveBalances();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetLeaveBalanceById() {
        when(leaveBalanceRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(leaveBalanceMapper.toDTo(entity)).thenReturn(dto);

        Optional<LeaveBalanceDTO> result = leaveBalanceService.getLeaveBalanceById(1L);
        assertEquals(dto, result);
    }

    @Test
    public void testCreateLeaveBalance() {
        when(leaveBalanceMapper.toEntity(dto)).thenReturn(entity);
        when(leaveBalanceRepository.save(entity)).thenReturn(entity);
        when(leaveBalanceMapper.toDTo(entity)).thenReturn(dto);

        LeaveBalanceDTO result = leaveBalanceService.createLeaveBalance(dto);
        assertEquals(dto, result);
    }

    @Test
    public void testUpdateLeaveBalance() {
        Long leaveBalanceId = 1L;
        when(leaveBalanceRepository.findById(leaveBalanceId)).thenReturn(Optional.of(existing));
        when(leaveBalanceMapper.toEntity(dto)).thenReturn(existing);
        when(leaveBalanceRepository.save(existing)).thenReturn(existing);
        when(leaveBalanceMapper.toDTo(existing)).thenReturn(dto);

        LeaveBalanceDTO result = leaveBalanceService.updateLeaveBalance(leaveBalanceId, dto);
        assertEquals(dto, result);
    }

    @Test
    public void testDeleteLeaveBalance() {
        Long leaveBalanceId = 1L;
        when(leaveBalanceRepository.existsById(leaveBalanceId)).thenReturn(true);
        leaveBalanceService.deleteLeaveBalance(leaveBalanceId);
        verify(leaveBalanceRepository, times(1)).deleteById(leaveBalanceId);
    }
}
