package com.leave.lams.service;

import java.time.LocalDate;
import java.util.List;

import com.leave.lams.dto.AttendanceDTO;

public interface AttendanceService {
	
	public void clockIn(Long employeeId);
	public void clockOut(Long employeeId);
	
	public List<AttendanceDTO> getAllAttendances();

	public AttendanceDTO getAttendanceById(Long attendanceId);

	public List<AttendanceDTO> getAttendanceByEmployee(Long employeeId);

	public AttendanceDTO addAttendance(AttendanceDTO attendance);

	public AttendanceDTO updateAttendance(Long id, AttendanceDTO attendanceDto);
	
	public void deleteAttendance(Long attendanceId);

	public List<AttendanceDTO> getAttendanceByDate(LocalDate date);

	public Double calculateWorkHours(Long attendanceId);
}
