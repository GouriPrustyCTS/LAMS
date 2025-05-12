package com.leave.lams.dao;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.AttendanceService;

@Service
public class AttendanceDAO implements AttendanceService {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceDAO.class);

	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Attendance> getAllAttendances() {
		logger.info("Getting all attendances");
		return attendanceRepository.findAll();
	}

	public Optional<Attendance> getAttendanceById(Long id) {
		logger.info("Getting attendance by id: {}", id);
		return attendanceRepository.findById(id);
	}

	public Attendance addAttendance(Attendance attendance) {
		logger.info("Adding attendance: {}", attendance);
		Attendance savedAttendance = attendanceRepository.save(attendance);
		logger.info("Attendance added with id: {}", savedAttendance.getAttendanceId());
		return savedAttendance;
	}

	public Attendance updatAttendance(long id, Attendance attendance) {
		logger.info("Updating attendance with id: {}, new attendance data: {}", id, attendance);
		Optional<Attendance> existingOptional = attendanceRepository.findById(id);
		if (existingOptional.isPresent()) {
			Attendance att = existingOptional.get();
			
			if(!att.getEmployee().getEmployeeId().equals(attendance.getEmployee().getEmployeeId())) {
				logger.error("Employee ID does not match the owner of this record.");
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}
			
			att.setClockInTime(attendance.getClockInTime());
			att.setClockOutTime(attendance.getClockOutTime());
			att.setAttendanceDate(attendance.getAttendanceDate());
			att.setWorkHours(attendance.getWorkHours());
			Attendance updatedAttendance = attendanceRepository.save(att);
			logger.info("Attendance updated with id: {}", updatedAttendance.getAttendanceId());
			return updatedAttendance;
		}
		logger.warn("Attendance with id: {} not found.", id);
		return null;
	}

	public void deleteAttendance(Long id) {
		logger.info("Deleting attendance with id: {}", id);
		attendanceRepository.deleteById(id);
		logger.info("Attendance with id: {} deleted.", id);
	}

	@Override
	public void clockIn(Long employeeId) {
		logger.info("Clocking in employee with id: {}", employeeId);
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		if (employee.isPresent()) {
			Attendance attendance = new Attendance();
			attendance.setEmployee(employee.get());
			attendance.setClockInTime(LocalDateTime.now());
			attendance.setAttendanceDate(LocalDate.now());
			Attendance savedAttendance = attendanceRepository.save(attendance); // Save here to get the generated ID
			logger.info("Employee with id: {} clocked in with attendance id: {}", employeeId, savedAttendance.getAttendanceId());
		} else {
			logger.error("Employee not found with ID: {}", employeeId);
			throw new RuntimeException("Employee not found with ID: " + employeeId);
		}
		
	}

	@Override
	public void clockOut(Long employeeId) {
		logger.info("Clocking out employee with id: {}", employeeId);
		List<Attendance> attendances = attendanceRepository.findLatestByEmployeeId(employeeId);
		if (!attendances.isEmpty()) {
			Attendance attendance = attendances.get(0);
			attendance.setClockOutTime(LocalDateTime.now());
			
			LocalDateTime clockInTime = attendance.getClockInTime();
			LocalDateTime clockOutTime = attendance.getClockOutTime();
			if (clockInTime != null && clockOutTime != null)
			{
				attendance.setWorkHours((double)(Duration.between(clockInTime, clockOutTime).toHours()));
			}
			else
			{
				attendance.setWorkHours(0.0);
				logger.warn("Clockin or Clockout time is null");
			}
			
			attendanceRepository.save(attendance);
			logger.info("Employee with id: {} clocked out.", employeeId);
		} else {
			logger.error("Clock-in record not found for employee id: {}", employeeId);
			throw new RuntimeException("Clock-in record not found");
		}
		
	}

	@Override	
	public List<Attendance> getAttendanceByEmployee(Long employeeId) {
		logger.info("Getting attendance by employee id: {}", employeeId);
		return attendanceRepository.findByEmployee_EmployeeId(employeeId);
	}

	@Override
	public List<Attendance> getAttendanceByDate(LocalDate date) {
		logger.info("Getting attendance by date: {}", date);
		return attendanceRepository.findByAttendanceDate(date);
	}

	@Override
	public Double calculateWorkHours(Long attendanceId) {
		logger.info("Calculating work hours for attendance id: {}", attendanceId);
		Optional<Attendance> attendanceOptional = getAttendanceById(attendanceId);
		if (attendanceOptional.isPresent()) {
			Attendance attendance = attendanceOptional.get();
			LocalDateTime clockInTime = attendance.getClockInTime();
			LocalDateTime clockOutTime = attendance.getClockOutTime();
			if (clockInTime != null && clockOutTime != null) {
				double workHours = (double) Duration.between(clockInTime, clockOutTime).toHours();
				logger.info("Work hours for attendance id: {} are: {}", attendanceId, workHours);
				return workHours;
			}
			else
			{
				logger.warn("Clockin or Clockout time is null");
				return 0.0;
			}
			
		}
		logger.error("Clock-out time not recorded for attendance id: {}", attendanceId);
		throw new RuntimeException("Clock-out time not recorded");
	}
	
	

}
