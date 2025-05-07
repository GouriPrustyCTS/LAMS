package com.leave.lams.service;

import java.util.List;

import com.leave.lams.model.Attendance;

public interface AttendanceService {
	public List<Attendance> getAllAttendances();

	public Attendance getAttendanceById(Long id);

	public Attendance addAttendance(Attendance attendance);

	public Attendance updatAttendance(long id, Attendance attendance);
	
	public void deleteAttendance(Long id);
}
