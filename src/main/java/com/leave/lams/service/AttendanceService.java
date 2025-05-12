package com.leave.lams.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.leave.lams.model.Attendance;

public interface AttendanceService {
	
	// In a concrete implementation, log the start of the clockIn operation
	public void clockIn(Long employeeId);
	// In a concrete implementation, log the start of the clockOut operation
	public void clockOut(Long employeeId);
	
	// In a concrete implementation, log the start and end of the getAllAttendances operation
	public List<Attendance> getAllAttendances();

	// In a concrete implementation, log the start and end of the getAttendanceById operation
	public Optional<Attendance> getAttendanceById(Long attendanceId);

	// In a concrete implementation, log the start and end of the getAttendanceByEmployee operation
	public List<Attendance> getAttendanceByEmployee(Long employeeId);

	// In a concrete implementation, log the start and end of the addAttendance operation
	public Attendance addAttendance(Attendance attendance);

	// In a concrete implementation, log the start and end of the updateAttendance operation
	public Attendance updatAttendance(long id, Attendance attendance);
	
	// In a concrete implementation, log the start and end of the deleteAttendance operation
	public void deleteAttendance(Long attendanceId);

	// In a concrete implementation, log the start and end of the getAttendanceByDate operation
	public List<Attendance> getAttendanceByDate(LocalDate date);

	// In a concrete implementation, log the start and end of the calculateWorkHours operation
	public Double calculateWorkHours(Long attendanceId);
}
