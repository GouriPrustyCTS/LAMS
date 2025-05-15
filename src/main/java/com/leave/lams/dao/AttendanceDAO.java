package com.leave.lams.dao;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.AttendanceDTO;
import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.mapper.AttendanceMapper;
import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.AttendanceService;

@Service
public class AttendanceDAO implements AttendanceService {


	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private AttendanceMapper mapper;

	public List<AttendanceDTO> getAllAttendances() {
		List<Attendance> attendances = attendanceRepository.findAll();
		return attendances.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	public Optional<AttendanceDTO> getAttendanceById(Long id) {
		Optional<Attendance> attendance = attendanceRepository.findById(id);
	    if (attendance.isPresent()) {
	        return Optional.of(mapper.toDTo(attendance.get()));
	    }
	    return Optional.empty();
	}

	public AttendanceDTO addAttendance(AttendanceDTO attendance) {
		Attendance att = mapper.toEntity(attendance);
		Attendance savedAttendance = attendanceRepository.save(att);
		AttendanceDTO dtoRes = mapper.toDTo(savedAttendance);
		return dtoRes;
	}

	public AttendanceDTO updatAttendance(long id, AttendanceDTO attendance) {
		Optional<Attendance> existingOptional = attendanceRepository.findById(id);
		if (existingOptional.isPresent()) {
			Attendance att = existingOptional.get();
			
			if(!att.getEmployee().getEmployeeId().equals(attendance.getEmployeeId())) {
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}
			
			att.setClockInTime(attendance.getClockInTime());
			att.setClockOutTime(attendance.getClockOutTime());
			att.setAttendanceDate(attendance.getAttendanceDate());
			att.setWorkHours(attendance.getWorkHours());
			Attendance updatedAttendance = attendanceRepository.save(att);
			return mapper.toDTo(updatedAttendance);
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
			Attendance savedAttendance = attendanceRepository.save(attendance); // Save here to get the generated ID
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
			
			LocalDateTime clockInTime = attendance.getClockInTime();
			LocalDateTime clockOutTime = attendance.getClockOutTime();
			if (clockInTime != null && clockOutTime != null)
			{
				attendance.setWorkHours((double)(Duration.between(clockInTime, clockOutTime).toHours()));
			}
			else
			{
				attendance.setWorkHours(0.0);
			}
			
			attendanceRepository.save(attendance);
		} else {
			throw new RuntimeException("Clock-in record not found");
		}
		
	}

	@Override	
	public List<AttendanceDTO> getAttendanceByEmployee(Long employeeId) {
		List<Attendance> attendances = attendanceRepository.findByEmployee_EmployeeId(employeeId);
		return attendances.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	@Override
	public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {
		List<Attendance> attendances = attendanceRepository.findByAttendanceDate(date);
		return attendances.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	@Override
	public Double calculateWorkHours(Long attendanceId) {
		Optional<AttendanceDTO> attendanceOptional = getAttendanceById(attendanceId);
		if (attendanceOptional.isPresent()) {
			AttendanceDTO attendance = attendanceOptional.get();
			LocalDateTime clockInTime = attendance.getClockInTime();
			LocalDateTime clockOutTime = attendance.getClockOutTime();
			if (clockInTime != null && clockOutTime != null) {
				double workHours = (double) Duration.between(clockInTime, clockOutTime).toHours();
				return workHours;
			}
			else
			{
				return 0.0;
			}
			
		}
		throw new RuntimeException("Clock-out time not recorded");
	}
	
	

}
