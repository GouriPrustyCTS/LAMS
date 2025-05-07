package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.AttendanceService;
import com.leave.lams.service.EmployeeService;

@Service
public class AttendanceDAO implements AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	public List<Attendance> getAllAttendances() {
		return attendanceRepository.findAll();
	}

	public Attendance getAttendanceById(Long id) {
		return attendanceRepository.findById(id).orElse(null);
	}

	public Attendance addAttendance(Attendance attendance) {
		return attendanceRepository.save(attendance);
	}

	public Attendance updatAttendance(long id, Attendance attendance) {
		Optional<Attendance> existingOptional = attendanceRepository.findById(id);
		if (existingOptional.isPresent()) {
			Attendance att = existingOptional.get();
			att.setClockInTime(attendance.getClockInTime());
			att.setClockOutTime(attendance.getClockOutTime());
			att.setAttendanceDate(attendance.getAttendanceDate());
			return attendanceRepository.save(att);
		}
		return null;
	}

	public void deleteAttendance(Long id) {
		attendanceRepository.deleteById(id);
	}

}