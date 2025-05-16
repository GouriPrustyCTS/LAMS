package com.leave.lams.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dao.AttendanceDAO;
import com.leave.lams.dto.AttendanceDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

	@Autowired
	private AttendanceDAO attendanceService;

	@GetMapping("/")
	public List<AttendanceDTO> getAllAttendances() {
		logger.info("Request received: GET /attendance/");
		List<AttendanceDTO> result = attendanceService.getAllAttendances();
		logger.info("Response sent: GET /attendance/ - Retrieved {} attendances.", result.size());
		return result;
	}

	@GetMapping("/{id}")
	public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Long id) {
		logger.info("Request received: GET /attendance/{}", id);
		AttendanceDTO attendance = attendanceService.getAttendanceById(id);
		if (attendance != null) {
			logger.info("Response sent: GET /attendance/{} - Attendance found.", id);
			return ResponseEntity.ok(attendance);
		} else {
			logger.warn("Response sent: GET /attendance/{} - Attendance NOT found.", id);
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/add")
	public AttendanceDTO addAttendance(@Valid @RequestBody AttendanceDTO attendance) {
		logger.info("Request received: POST /attendance/add - Request Body: {}", attendance);
		AttendanceDTO result = attendanceService.addAttendance(attendance);
		if (result != null) {
			logger.info("Response sent: POST /attendance/add - Attendance added with ID: {}", result.getAttendanceId());
		} else {
			logger.warn("Response sent: POST /attendance/add - Attendance add failed.");
		}
		return result;
	}

	@PutMapping("/{id}")
	public AttendanceDTO updatAttendance(@PathVariable Long id,@Valid @RequestBody AttendanceDTO attendance) {
		logger.info("Request received: PUT /attendance/{} - Request Body: {}", id, attendance);
		AttendanceDTO result = attendanceService.updatAttendance(id, attendance);
		logger.info("Response sent: PUT /attendance/{} - Attendance updated.", id);
		return result;
	}

	@DeleteMapping("/{id}")
	public void deleteAttendance(@PathVariable Long id) {
		logger.info("Request received: DELETE /attendance/{}", id);
		attendanceService.deleteAttendance(id);
		logger.info("Response sent: DELETE /attendance/{} - Attendance deleted.", id);
	}

	// Clock in an employee
	@PostMapping("/clock-in/{employeeId}")
	public ResponseEntity<String> clockIn(@PathVariable long employeeId) {
		logger.info("Request received: POST /attendance/clock-in/{}", employeeId);
		try {
			attendanceService.clockIn(employeeId);
			logger.info("Response sent: POST /attendance/clock-in/{} - Employee clocked in.", employeeId);
			return ResponseEntity.ok("Employee clocked in successfully.");
		} catch (RuntimeException e) {
			logger.error("Error in clockIn for employeeId: {} - {}", employeeId, e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Clock out an employee
	@PostMapping("/clock-out/{employeeId}")
	public ResponseEntity<String> clockOut(@PathVariable long employeeId) {
		logger.info("Request received: POST /attendance/clock-out/{}", employeeId);
		try {
			attendanceService.clockOut(employeeId);
			logger.info("Response sent: POST /attendance/clock-out/{} - Employee clocked out.", employeeId);
			return ResponseEntity.ok("Employee clocked out successfully.");
		} catch (RuntimeException e) {
			logger.error("Error in clockOut for employeeId: {} - {}", employeeId, e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Get attendance records for a specific employee
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByEmployee(@PathVariable long employeeId) {
		logger.info("Request received: GET /attendance/employee/{}", employeeId);
		List<AttendanceDTO> result = attendanceService.getAttendanceByEmployee(employeeId);
		logger.info("Response sent: GET /attendance/employee/{} - Retrieved {} attendances.", employeeId, result.size());
		return ResponseEntity.ok(result);
	}

	// Get attendance by a specific date -> 	../date/2025-05-10
	@GetMapping("/date/{date}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(@PathVariable LocalDate date) {
		logger.info("Request received: GET /attendance/date/{}", date);
		List<AttendanceDTO> result = attendanceService.getAttendanceByDate(date);
		logger.info("Response sent: GET /attendance/date/{} - Retrieved {} attendances.", date, result.size());
		return ResponseEntity.ok(result);
	}

	// Calculate work hours for an attendance record
	@GetMapping("/hours/{attendanceId}")
	public ResponseEntity<Double> calculateWorkHours(@PathVariable long attendanceId) {
		logger.info("Request received: GET /attendance/hours/{}", attendanceId);
		try {
			Double result = attendanceService.calculateWorkHours(attendanceId);
			logger.info("Response sent: GET /attendance/hours/{} - Work hours: {}", attendanceId, result);
			return ResponseEntity.ok(result);
		} catch (RuntimeException e) {
			logger.error("Error in calculateWorkHours for attendanceId: {} - {}", attendanceId, e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

}
