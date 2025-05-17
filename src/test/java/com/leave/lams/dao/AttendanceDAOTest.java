package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private AttendanceMapper attendanceMapper;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllAttendances() {
        AttendanceDTO dto1 = new AttendanceDTO();
        AttendanceDTO dto2 = new AttendanceDTO();
        Attendance entity1 = new Attendance();
        Attendance entity2 = new Attendance();

        when(attendanceRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(attendanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(attendanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<AttendanceDTO> result = attendanceService.getAllAttendances();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceById() {
        AttendanceDTO dto = new AttendanceDTO();
        Attendance entity = new Attendance();

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(attendanceMapper.toDTo(entity)).thenReturn(dto);

        AttendanceDTO result = attendanceService.getAttendanceById(1L);
        assertEquals(dto, result);
    }

    @Test
    public void testAddAttendance() {
        Attendance entity = new Attendance();
        Employee emp = new Employee();
        emp.setEmployeeId(1L);
        entity.setEmployee(emp);  // <-- Add employee to avoid exception

        AttendanceDTO dto = new AttendanceDTO();

        when(attendanceMapper.toEntity(dto)).thenReturn(entity);
        when(attendanceRepository.save(entity)).thenReturn(entity);
        when(attendanceMapper.toDTo(entity)).thenReturn(dto);

        AttendanceDTO result = attendanceService.addAttendance(dto);
        assertEquals(dto, result);
    }


    @Test
    public void testUpdateAttendance() {
        Attendance existing = new Attendance();
        Employee emp = new Employee();
        emp.setEmployeeId(1L);
        existing.setEmployee(emp);

        AttendanceDTO dto = new AttendanceDTO();

        Attendance updated = new Attendance();
        updated.setEmployee(emp);  // <-- employee ID matches existing
        updated.setClockInTime(LocalDateTime.now());
        updated.setClockOutTime(LocalDateTime.now().plusHours(8));
        updated.setAttendanceDate(LocalDate.now());
        updated.setWorkHours(8.0);

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(attendanceMapper.toEntity(dto)).thenReturn(updated);
        when(attendanceRepository.save(existing)).thenReturn(existing);
        when(attendanceMapper.toDTo(existing)).thenReturn(dto);

        AttendanceDTO result = attendanceService.updatAttendance(1L, dto);
        assertEquals(dto, result);
    }


    @Test
    public void testDeleteAttendance() {
        Attendance attendance = new Attendance();  // mock attendance object
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance)); // <-- this line is required
        doNothing().when(attendanceRepository).deleteById(1L);
        attendanceService.deleteAttendance(1L);
        verify(attendanceRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testClockIn() {
        Employee emp = new Employee();
        emp.setEmployeeId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(new Attendance());

        attendanceService.clockIn(1L);

        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    public void testClockOut() {
        Attendance attendance = new Attendance();
        attendance.setClockInTime(LocalDateTime.now().minusHours(8));

        when(attendanceRepository.findLatestByEmployeeId(1L)).thenReturn(Arrays.asList(attendance));
        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        attendanceService.clockOut(1L);

        verify(attendanceRepository, times(1)).save(attendance);
        assertNotNull(attendance.getClockOutTime());
        assertEquals(8.0, attendance.getWorkHours());
    }

    @Test
    public void testGetAttendanceByEmployee() {
        Attendance entity1 = new Attendance();
        Attendance entity2 = new Attendance();
        AttendanceDTO dto1 = new AttendanceDTO();
        AttendanceDTO dto2 = new AttendanceDTO();

        when(attendanceRepository.findByEmployee_EmployeeId(1L)).thenReturn(Arrays.asList(entity1, entity2));
        when(attendanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(attendanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<AttendanceDTO> result = attendanceService.getAttendanceByEmployee(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceByDate() {
        Attendance entity1 = new Attendance();
        Attendance entity2 = new Attendance();
        AttendanceDTO dto1 = new AttendanceDTO();
        AttendanceDTO dto2 = new AttendanceDTO();

        when(attendanceRepository.findByAttendanceDate(LocalDate.now())).thenReturn(Arrays.asList(entity1, entity2));
        when(attendanceMapper.toDTo(entity1)).thenReturn(dto1);
        when(attendanceMapper.toDTo(entity2)).thenReturn(dto2);

        List<AttendanceDTO> result = attendanceService.getAttendanceByDate(LocalDate.now());
        assertEquals(2, result.size());
    }

    @Test
    public void testCalculateWorkHours() {
        Attendance entity = new Attendance();
        entity.setClockInTime(LocalDateTime.now().minusHours(8));
        entity.setClockOutTime(LocalDateTime.now());

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(entity));

        Double result = attendanceService.calculateWorkHours(1L);
        assertEquals(8.0, result, 0.01);
    }
}
