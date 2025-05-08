package com.leave.lams.service;

import java.time.LocalDate;
import java.util.List;

import com.leave.lams.model.Attendance;

public interface AttendanceService {

	public void clockIn(Long employeeId);

	public void clockOut(Long employeeId);

	public List<Attendance> getAllAttendances();

	public Attendance getAttendanceById(Long attendanceId);

	public List<Attendance> getAttendanceByEmployee(Long employeeId);

	public Attendance addAttendance(Attendance attendance);

	public Attendance updatAttendance(long id, Attendance attendance);

	public void deleteAttendance(Long id);

	public List<Attendance> getAttendanceByDate(LocalDate date);

	public Double calculateWorkHours(Long attendanceId);
}

