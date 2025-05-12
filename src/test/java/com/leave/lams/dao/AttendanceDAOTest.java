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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;

public class AttendanceDAOTest {

    @InjectMocks
    private AttendanceDAO attendanceDAO;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAttendances() {
        List<Attendance> attendances = Arrays.asList(new Attendance(), new Attendance());
        when(attendanceRepository.findAll()).thenReturn(attendances);

        List<Attendance> result = attendanceDAO.getAllAttendances();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceById() {
        Attendance attendance = new Attendance();
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        Optional<Attendance> result = attendanceDAO.getAttendanceById(1L);
        assertTrue(result.isPresent());
        assertEquals(attendance, result.get());
    }

    @Test
    public void testAddAttendance() {
        Attendance attendance = new Attendance();
        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        Attendance result = attendanceDAO.addAttendance(attendance);
        assertEquals(attendance, result);
    }

    @Test
    public void testUpdateAttendance() {
        Attendance existingAttendance = new Attendance();
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        existingAttendance.setEmployee(employee);

        Attendance newAttendance = new Attendance();
        newAttendance.setEmployee(employee);
        newAttendance.setClockInTime(LocalDateTime.now());
        newAttendance.setClockOutTime(LocalDateTime.now().plusHours(8));
        newAttendance.setAttendanceDate(LocalDate.now());
        newAttendance.setWorkHours(8.0);

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(existingAttendance));
        when(attendanceRepository.save(existingAttendance)).thenReturn(existingAttendance);

        Attendance result = attendanceDAO.updatAttendance(1L, newAttendance);
        assertEquals(existingAttendance, result);
        assertEquals(newAttendance.getClockInTime(), result.getClockInTime());
        assertEquals(newAttendance.getClockOutTime(), result.getClockOutTime());
        assertEquals(newAttendance.getAttendanceDate(), result.getAttendanceDate());
        assertEquals(newAttendance.getWorkHours(), result.getWorkHours());
    }

    @Test
    public void testDeleteAttendance() {
        doNothing().when(attendanceRepository).deleteById(1L);

        attendanceDAO.deleteAttendance(1L);
        verify(attendanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testClockIn() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        attendanceDAO.clockIn(1L);
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    public void testClockOut() {
        Attendance attendance = new Attendance();
        attendance.setClockInTime(LocalDateTime.now().minusHours(8));
        when(attendanceRepository.findLatestByEmployeeId(1L)).thenReturn(Arrays.asList(attendance));

        attendanceDAO.clockOut(1L);
        verify(attendanceRepository, times(1)).save(attendance);
        assertNotNull(attendance.getClockOutTime());
        assertEquals(8.0, attendance.getWorkHours());
    }

    @Test
    public void testGetAttendanceByEmployee() {
        List<Attendance> attendances = Arrays.asList(new Attendance(), new Attendance());
        when(attendanceRepository.findByEmployee_EmployeeId(1L)).thenReturn(attendances);

        List<Attendance> result = attendanceDAO.getAttendanceByEmployee(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAttendanceByDate() {
        List<Attendance> attendances = Arrays.asList(new Attendance(), new Attendance());
        when(attendanceRepository.findByAttendanceDate(LocalDate.now())).thenReturn(attendances);

        List<Attendance> result = attendanceDAO.getAttendanceByDate(LocalDate.now());
        assertEquals(2, result.size());
    }

    @Test
    public void testCalculateWorkHours() {
        Attendance attendance = new Attendance();
        attendance.setClockInTime(LocalDateTime.now().minusHours(8));
        attendance.setClockOutTime(LocalDateTime.now());
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        Double result = attendanceDAO.calculateWorkHours(1L);
        assertEquals(8.0, result,0.01);
    }
}
