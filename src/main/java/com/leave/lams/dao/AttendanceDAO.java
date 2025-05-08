package com.leave.lams.dao;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.AttendanceService;

@Service
public class AttendanceDAO implements AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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

	@Override
	public void clockIn(Long employeeId) {
		Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setEmployee(employee.get());
            attendance.setClockInTime(LocalDateTime.now());
            attendance.setAttendanceDate(LocalDate.now());
            attendanceRepository.save(attendance);
        } else {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
		
	}

	@Override
	public void clockOut(Long employeeId) {
		List<Attendance> attendances = attendanceRepository.findLatestByEmployeeId(employeeId);
        if (!attendances.isEmpty()) {
            Attendance attendance = attendances.get(0);
            attendance.setClockOutTime(LocalDateTime.now());
            attendance.setWorkHours((double)(Duration.between(attendance.getClockInTime(), attendance.getClockOutTime()).toHours()));
            attendanceRepository.save(attendance);
        } else {
            throw new RuntimeException("Clock-in record not found");
        }
		
	}

	@Override
	public List<Attendance> getAttendanceByEmployee(Long employeeId) {
		return attendanceRepository.findByEmployeeId(employeeId);
	}

	@Override
	public List<Attendance> getAttendanceByDate(LocalDate date) {
		return attendanceRepository.findByAttendanceDate(date);
	}

	@Override
	public Double calculateWorkHours(Long attendanceId) {
		Attendance attendance = getAttendanceById(attendanceId);
        if (attendance.getClockOutTime() != null) {
            return (double) Duration.between(attendance.getClockInTime(), attendance.getClockOutTime()).toHours();
        }
        throw new RuntimeException("Clock-out time not recorded");
	}

}