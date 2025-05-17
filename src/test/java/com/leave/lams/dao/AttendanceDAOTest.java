package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave.lams.dto.AttendanceDTO;
import com.leave.lams.mapper.AttendanceMapper;
import com.leave.lams.model.Attendance;
import com.leave.lams.model.AttendanceDTO;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.AttendanceService;

public class AttendanceDAOTest {

    @InjectMocks
    private AttendanceService attendanceService;

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
        List<AttendanceDTO> attendances = Arrays.asList(new AttendanceDTO(), new AttendanceDTO());
        when(attendanceRepository.findAll()).thenReturn(attendances.stream().map((a)->attendanceMapper.toEntity(a)).collect(Collectors.toList()));

        List<AttendanceDTO> result = attendanceService.getAllAttendances();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceById() {
        AttendanceDTO attendance = new AttendanceDTO();
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendanceMapper.toEntity(attendance)));

        AttendanceDTO result = attendanceService.getAttendanceById(1L);
        assertEquals(attendance, result);
    }

    @Test
    public void testAddAttendance() {
        AttendanceDTO attendance = new AttendanceDTO();
        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        AttendanceDTO result = attendanceService.addAttendance(attendance);
        assertEquals(attendance, result);
    }

    @Test
    public void testUpdateAttendance() {
        AttendanceDTO existingAttendance = new AttendanceDTO();
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        existingAttendance.setEmployee(employee);

        AttendanceDTO newAttendance = new AttendanceDTO();
        newAttendance.setEmployee(employee);
        newAttendance.setClockInTime(LocalDateTime.now());
        newAttendance.setClockOutTime(LocalDateTime.now().plusHours(8));
        newAttendance.setAttendanceDate(LocalDate.now());
        newAttendance.setWorkHours(8.0);

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(existingAttendance));
        when(attendanceRepository.save(existingAttendance)).thenReturn(existingAttendance);

        AttendanceDTO result = attendanceService.updatAttendance(1L, newAttendance);
        assertEquals(existingAttendance, result);
        assertEquals(newAttendance.getClockInTime(), result.getClockInTime());
        assertEquals(newAttendance.getClockOutTime(), result.getClockOutTime());
        assertEquals(newAttendance.getAttendanceDate(), result.getAttendanceDate());
        assertEquals(newAttendance.getWorkHours(), result.getWorkHours());
    }

    @Test
    public void testDeleteAttendance() {
        doNothing().when(attendanceRepository).deleteById(1L);

        attendanceService.deleteAttendance(1L);
        verify(attendanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testClockIn() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        attendanceService.clockIn(1L);
        verify(attendanceRepository, times(1)).save(any(AttendanceDTO.class));
    }

    @Test
    public void testClockOut() {
        AttendanceDTO attendance = new AttendanceDTO();
        attendance.setClockInTime(LocalDateTime.now().minusHours(8));
        when(attendanceRepository.findLatestByEmployeeId(1L)).thenReturn(Arrays.asList(attendance));

        attendanceService.clockOut(1L);
        verify(attendanceRepository, times(1)).save(attendance);
        assertNotNull(attendance.getClockOutTime());
        assertEquals(8.0, attendance.getWorkHours());
    }

    @Test
    public void testGetAttendanceByEmployee() {
        List<AttendanceDTO> attendances = Arrays.asList(new AttendanceDTO(), new AttendanceDTO());
        when(attendanceRepository.findByEmployee_EmployeeId(1L)).thenReturn(attendances);

        List<AttendanceDTO> result = attendanceService.getAttendanceByEmployee(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceByDate() {
        List<AttendanceDTO> attendances = Arrays.asList(new AttendanceDTO(), new AttendanceDTO());
        when(attendanceRepository.findByAttendanceDate(LocalDate.now())).thenReturn(attendances);

        List<AttendanceDTO> result = attendanceService.getAttendanceByDate(LocalDate.now());
        assertEquals(2, result.size());
    }

    @Test
    public void testCalculateWorkHours() {
        AttendanceDTO attendance = new AttendanceDTO();
        attendance.setClockInTime(LocalDateTime.now().minusHours(8));
        attendance.setClockOutTime(LocalDateTime.now());
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        Double result = attendanceService.calculateWorkHours(1L);
        assertEquals(8.0, result,0.01);
    }
}
