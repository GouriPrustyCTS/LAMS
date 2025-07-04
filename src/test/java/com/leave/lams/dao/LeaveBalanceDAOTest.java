package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leave.lams.dto.LeaveBalanceDTO;
import com.leave.lams.exception.LeaveBalanceNotFoundException;
import com.leave.lams.mapper.LeaveBalanceMapper;
import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;

@ExtendWith(MockitoExtension.class)
class LeaveBalanceDAOTest {

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @Mock
    private LeaveBalanceMapper mapper;

    @InjectMocks
    private LeaveBalanceDAO leaveBalanceDAO;

    private LeaveBalance leaveBalance;
    private LeaveBalanceDTO leaveBalanceDTO;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("HR");
        employee.setJobTitle("Manager");
        employee.setHireDate(new Date());
        employee.setPassword("hashedpassword");

        leaveBalance = new LeaveBalance();
        leaveBalance.setLeaveBalanceId(100L); // Corrected to leaveBalanceId
        leaveBalance.setLeaveType("Annual Leave");
        leaveBalance.setBalance(15.0);
        leaveBalance.setEmployee(employee);

        leaveBalanceDTO = new LeaveBalanceDTO();
        leaveBalanceDTO.setLeaveBalanceId(100L); // This ID maps to the LeaveBalance's ID
        leaveBalanceDTO.setLeaveType("Annual Leave");
        leaveBalanceDTO.setBalance(15.0);
        leaveBalanceDTO.setEmployeeId(1L);
    }

    @Test
    void testCreateLeaveBalance() {
        when(mapper.toEntity(any(LeaveBalanceDTO.class))).thenReturn(leaveBalance);
        when(leaveBalanceRepository.save(any(LeaveBalance.class))).thenReturn(leaveBalance);
        when(mapper.toDTo(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO);

        LeaveBalanceDTO result = leaveBalanceDAO.createLeaveBalance(leaveBalanceDTO);

        assertNotNull(result);
        assertEquals(leaveBalanceDTO.getLeaveBalanceId(), result.getLeaveBalanceId());
        assertEquals(leaveBalanceDTO.getLeaveType(), result.getLeaveType());
        assertEquals(leaveBalanceDTO.getBalance(), result.getBalance());

        verify(mapper, times(1)).toEntity(leaveBalanceDTO);
        verify(leaveBalanceRepository, times(1)).save(leaveBalance);
        verify(mapper, times(1)).toDTo(leaveBalance);
    }

    @Test
    void testGetAllLeaveBalances() {
        List<LeaveBalance> leaveBalances = Arrays.asList(leaveBalance);
        List<LeaveBalanceDTO> leaveBalanceDTOs = Arrays.asList(leaveBalanceDTO);

        when(leaveBalanceRepository.findAll()).thenReturn(leaveBalances);
        when(mapper.toDTo(leaveBalance)).thenReturn(leaveBalanceDTO);

        List<LeaveBalanceDTO> result = leaveBalanceDAO.getAllLeaveBalances();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(leaveBalanceDTOs.get(0).getLeaveBalanceId(), result.get(0).getLeaveBalanceId());

        verify(leaveBalanceRepository, times(1)).findAll();
        verify(mapper, times(1)).toDTo(leaveBalance);
    }

    @Test
    void testGetLeaveBalanceById_Found() {
        // The DAO method getLeaveBalanceById takes 'employeeID' but uses it as leaveBalanceId for findById.
        // So, we use leaveBalanceId here for consistency with the repository call.
        Long leaveBalanceId = 100L;
        when(leaveBalanceRepository.findById(leaveBalanceId)).thenReturn(Optional.of(leaveBalance));
        when(mapper.toDTo(leaveBalance)).thenReturn(leaveBalanceDTO);

        Optional<LeaveBalanceDTO> result = leaveBalanceDAO.getLeaveBalanceById(leaveBalanceId);

        assertTrue(result.isPresent());
        assertEquals(leaveBalanceDTO.getLeaveBalanceId(), result.get().getLeaveBalanceId());

        verify(leaveBalanceRepository, times(1)).findById(leaveBalanceId);
        verify(mapper, times(1)).toDTo(leaveBalance);
    }

    @Test
    void testGetLeaveBalanceById_NotFound() {
        Long nonExistentLeaveBalanceId = 999L;
        when(leaveBalanceRepository.findById(nonExistentLeaveBalanceId)).thenReturn(Optional.empty());

        assertThrows(LeaveBalanceNotFoundException.class, () -> leaveBalanceDAO.getLeaveBalanceById(nonExistentLeaveBalanceId));

        verify(leaveBalanceRepository, times(1)).findById(nonExistentLeaveBalanceId);
        verify(mapper, never()).toDTo(any(LeaveBalance.class));
    }

    @Test
    void testUpdateLeaveBalanceByLeaveId_Found() {
        Long leaveId = 100L;
        LeaveBalanceDTO updatedLeaveBalanceDTO = new LeaveBalanceDTO();
        updatedLeaveBalanceDTO.setLeaveBalanceId(leaveId);
        updatedLeaveBalanceDTO.setLeaveType("Sick Leave");
        updatedLeaveBalanceDTO.setBalance(5.0);
        updatedLeaveBalanceDTO.setEmployeeId(1L);

        // Mock the existing entity that would be found
        LeaveBalance existingLeaveBalance = new LeaveBalance();
        existingLeaveBalance.setLeaveBalanceId(leaveId);
        existingLeaveBalance.setLeaveType("Annual Leave"); // Original type
        existingLeaveBalance.setBalance(15.0); // Original balance
        existingLeaveBalance.setEmployee(employee);

        // Mock the entity after it's updated and saved
        LeaveBalance savedLeaveBalance = new LeaveBalance();
        savedLeaveBalance.setLeaveBalanceId(leaveId);
        savedLeaveBalance.setLeaveType("Sick Leave"); // Updated type
        savedLeaveBalance.setBalance(5.0); // Updated balance
        savedLeaveBalance.setEmployee(employee);


        when(leaveBalanceRepository.findById(leaveId)).thenReturn(Optional.of(existingLeaveBalance));
        when(leaveBalanceRepository.save(any(LeaveBalance.class))).thenReturn(savedLeaveBalance);
        when(mapper.toDTo(savedLeaveBalance)).thenReturn(updatedLeaveBalanceDTO);

        LeaveBalanceDTO result = leaveBalanceDAO.updateLeaveBalanceByLeaveId(leaveId, updatedLeaveBalanceDTO);

        assertNotNull(result);
        assertEquals(updatedLeaveBalanceDTO.getLeaveType(), result.getLeaveType());
        assertEquals(updatedLeaveBalanceDTO.getBalance(), result.getBalance());
        assertEquals(updatedLeaveBalanceDTO.getLeaveBalanceId(), result.getLeaveBalanceId());
        assertEquals(updatedLeaveBalanceDTO.getEmployeeId(), result.getEmployeeId());

        verify(leaveBalanceRepository, times(1)).findById(leaveId);
        // Verify that save was called with the modified existing entity
        verify(leaveBalanceRepository, times(1)).save(existingLeaveBalance);
        verify(mapper, times(1)).toDTo(savedLeaveBalance);
    }

    @Test
    void testUpdateLeaveBalanceByLeaveId_NotFound() {
        Long nonExistentId = 999L;
        when(leaveBalanceRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(LeaveBalanceNotFoundException.class, () -> leaveBalanceDAO.updateLeaveBalanceByLeaveId(nonExistentId, leaveBalanceDTO));

        verify(leaveBalanceRepository, times(1)).findById(nonExistentId);
        verify(leaveBalanceRepository, never()).save(any(LeaveBalance.class));
        verify(mapper, never()).toDTo(any(LeaveBalance.class));
    }



    @Test
    void testUpdateLeaveBalancesByEmployeeId_NotFound() {
        Long nonExistentEmployeeId = 999L;
        when(leaveBalanceRepository.findAllByEmployee_EmployeeId(nonExistentEmployeeId)).thenReturn(Optional.empty());

        assertThrows(LeaveBalanceNotFoundException.class, () -> leaveBalanceDAO.updateLeaveBalancesByEmployeeId(nonExistentEmployeeId, leaveBalanceDTO));

        verify(leaveBalanceRepository, times(1)).findAllByEmployee_EmployeeId(nonExistentEmployeeId);
        verify(leaveBalanceRepository, never()).saveAll(anyList());
        verify(mapper, never()).toDTo(any(LeaveBalance.class));
    }

    @Test
    void testDeleteLeaveBalance_Exists() {
        Long leaveBalanceId = 100L;
        when(leaveBalanceRepository.existsById(leaveBalanceId)).thenReturn(true);

        leaveBalanceDAO.deleteLeaveBalance(leaveBalanceId);

        verify(leaveBalanceRepository, times(1)).existsById(leaveBalanceId);
        verify(leaveBalanceRepository, times(1)).deleteById(leaveBalanceId);
    }

    @Test
    void testDeleteLeaveBalance_NotFound() {
        Long nonExistentId = 999L;
        when(leaveBalanceRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(LeaveBalanceNotFoundException.class, () -> leaveBalanceDAO.deleteLeaveBalance(nonExistentId));

        verify(leaveBalanceRepository, times(1)).existsById(nonExistentId);
        verify(leaveBalanceRepository, never()).deleteById(anyLong());
    }

    @Test
    void testCheckEmployeeExists_True() {
        Long employeeId = 1L;
        when(leaveBalanceRepository.existsByEmployee_EmployeeId(employeeId)).thenReturn(true);

        boolean exists = leaveBalanceDAO.checkEmployeeExists(employeeId);

        assertTrue(exists);

        verify(leaveBalanceRepository, times(1)).existsByEmployee_EmployeeId(employeeId);
    }

    @Test
    void testCheckEmployeeExists_False() {
        Long nonExistentEmployeeId = 999L;
        when(leaveBalanceRepository.existsByEmployee_EmployeeId(nonExistentEmployeeId)).thenReturn(false);

        boolean exists = leaveBalanceDAO.checkEmployeeExists(nonExistentEmployeeId);

        assertFalse(exists);

        verify(leaveBalanceRepository, times(1)).existsByEmployee_EmployeeId(nonExistentEmployeeId);
    }
}