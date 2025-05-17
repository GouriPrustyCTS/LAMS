package com.leave.lams.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.leave.lams.model.Attendance;

public class AttendanceDTOTest {

    @Test
    public void testAttendanceDTOCreation() {
        // Arrange
        Long attendanceId = 1L;
        Long employeeId = 101L;
        LocalDate attendanceDate = LocalDate.now();
        LocalDateTime clockInTime = LocalDateTime.now().minusHours(8);
        LocalDateTime clockOutTime = LocalDateTime.now();
        double workHours = 8.0;

        // Act
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setAttendanceId(attendanceId);
        attendanceDTO.setEmployeeId(employeeId);
        attendanceDTO.setAttendanceDate(attendanceDate);
        attendanceDTO.setClockInTime(clockInTime);
        attendanceDTO.setClockOutTime(clockOutTime);
        attendanceDTO.setWorkHours(workHours);

        // Assert
        assertEquals(attendanceId, attendanceDTO.getAttendanceId());
        assertEquals(employeeId, attendanceDTO.getEmployeeId());
        assertEquals(attendanceDate, attendanceDTO.getAttendanceDate());
        assertEquals(clockInTime, attendanceDTO.getClockInTime());
        assertEquals(clockOutTime, attendanceDTO.getClockOutTime());
        assertEquals(workHours, attendanceDTO.getWorkHours());
    }

    @Test
    public void testAttendanceDTOFromModel() {
        // Arrange
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(1L);
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setClockInTime(LocalDateTime.now().minusHours(8));
        attendance.setClockOutTime(LocalDateTime.now());
        attendance.setWorkHours(8.0);

        // Act
        AttendanceDTO attendanceDTO = new AttendanceDTO();

        // Assert
        assertNotNull(attendanceDTO);
        assertEquals(attendance.getAttendanceId(), attendanceDTO.getAttendanceId());
        assertEquals(attendance.getAttendanceDate(), attendanceDTO.getAttendanceDate());
        assertEquals(attendance.getClockInTime(), attendanceDTO.getClockInTime());
        assertEquals(attendance.getClockOutTime(), attendanceDTO.getClockOutTime());
        assertEquals(attendance.getWorkHours(), attendanceDTO.getWorkHours());
    }
}