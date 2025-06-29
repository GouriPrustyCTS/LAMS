package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.leave.lams.dto.AttendanceDTO;
import com.leave.lams.mapper.AttendanceMapper;
import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class AttendanceDAOTest {

    @InjectMocks
    private AttendanceDAO attendanceService; // Concrete class

    @InjectMocks
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceMapper attendanceMapper;

    private AttendanceDTO dto1, dto2, dto;
    private Attendance entity1, entity2, entity, existing;
    private Employee emp;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new AttendanceDTO();
        entity = new Attendance();
        existing = new Attendance();

        emp = new Employee();
        emp.setEmployeeId(1L);
        entity.setEmployee(emp);
        existing.setEmployee(emp);
        
        dto.setEmployeeId(1L);
        dto.setClockInTime(LocalDateTime.now());
        dto.setClockOutTime(LocalDateTime.now().plusHours(8));
        dto.setAttendanceDate(LocalDate.now());
        dto.setWorkHours(8.0);
    }

    @Test
    public void testGetAllAttendances() {
        when(attendanceRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(attendanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(attendanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<AttendanceDTO> result = attendanceService.getAllAttendances();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceById() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(attendanceMapper.toDTo(entity)).thenReturn(dto);

        AttendanceDTO result = attendanceService.getAttendanceById(1L);
        assertEquals(dto, result);
    }

    @Test
    public void testAddAttendance() {
        when(attendanceMapper.toEntity(dto)).thenReturn(entity);
        when(attendanceRepository.save(entity)).thenReturn(entity);
        when(attendanceMapper.toDTo(entity)).thenReturn(dto);

        AttendanceDTO result = attendanceService.addAttendance(dto);
        assertEquals(dto, result);
    }

    @Test
    public void testUpdateAttendance() {
        Long attendanceId = 1L;
        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(existing));
        when(attendanceMapper.toEntity(dto)).thenReturn(existing);
        when(attendanceRepository.save(existing)).thenReturn(existing);
        when(attendanceMapper.toDTo(existing)).thenReturn(dto);

        AttendanceDTO result = attendanceService.updateAttendance(attendanceId, dto);
        assertEquals(dto, result);
    }

    @Test
    public void testDeleteAttendance() {
        Long attendanceId = 1L;
        when(attendanceRepository.existsById(attendanceId)).thenReturn(true);
        attendanceService.deleteAttendance(attendanceId);
        verify(attendanceRepository, times(1)).deleteById(attendanceId);
    }
    
    @Test
    public void testGetAttendanceByEmployee() {
        when(attendanceRepository.findByEmployee_EmployeeId(1L)).thenReturn(Arrays.asList(entity1, entity2));
        when(attendanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(attendanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<AttendanceDTO> result = attendanceService.getAttendanceByEmployee(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceByDate() {
        when(attendanceRepository.findByAttendanceDate(LocalDate.now())).thenReturn(Arrays.asList(entity1, entity2));
        when(attendanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(attendanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<AttendanceDTO> result = attendanceService.getAttendanceByDate(LocalDate.now());
        assertEquals(2, result.size());
    }
    
    @Test
    public void testCalculateWorkHours() {
        entity.setClockInTime(LocalDateTime.now().minusHours(8));
        entity.setClockOutTime(LocalDateTime.now());

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(entity));

        Double result = attendanceService.calculateWorkHours(1L);
        assertEquals(8.0, result, 0.01);
    }
}