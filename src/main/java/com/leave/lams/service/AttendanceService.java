package com.leave.lams.service;

import java.time.LocalDate;
import java.util.List;

import com.leave.lams.dto.AttendanceDTO;

public interface AttendanceService {
	
	// In a concrete implementation, log the start of the clockIn operation
	public void clockIn(Long employeeId);
	// In a concrete implementation, log the start of the clockOut operation
	public void clockOut(Long employeeId);
	
	// In a concrete implementation, log the start and end of the getAllAttendances operation
	public List<AttendanceDTO> getAllAttendances();

	// In a concrete implementation, log the start and end of the getAttendanceById operation
	public AttendanceDTO getAttendanceById(Long attendanceId);

	// In a concrete implementation, log the start and end of the getAttendanceByEmployee operation
	public List<AttendanceDTO> getAttendanceByEmployee(Long employeeId);

	// In a concrete implementation, log the start and end of the addAttendance operation
	public AttendanceDTO addAttendance(AttendanceDTO attendance);

	// In a concrete implementation, log the start and end of the updateAttendance operation
	public AttendanceDTO updatAttendance(Long id, AttendanceDTO attendanceDto);
	
	// In a concrete implementation, log the start and end of the deleteAttendance operation
	public void deleteAttendance(Long attendanceId);

	// In a concrete implementation, log the start and end of the getAttendanceByDate operation
	public List<AttendanceDTO> getAttendanceByDate(LocalDate date);

	// In a concrete implementation, log the start and end of the calculateWorkHours operation
	public Double calculateWorkHours(Long attendanceId);
}
