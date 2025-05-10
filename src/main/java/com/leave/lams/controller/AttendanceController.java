package com.leave.lams.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.leave.lams.model.Attendance;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceDAO attendanceService;

	@GetMapping("/")
	public List<Attendance> getAllAttendances() {
		return attendanceService.getAllAttendances();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
		return attendanceService.getAttendanceById(id)
	                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/add")
	public Attendance addAttendance(@RequestBody Attendance attendance) {
		return attendanceService.addAttendance(attendance);
	}

	@PutMapping("/{id}")
	public Attendance updatAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
		return attendanceService.updatAttendance(id, attendance);
	}

	@DeleteMapping("/{id}")
	public void deleteAttendance(@PathVariable Long id) {
		attendanceService.deleteAttendance(id);
	}
	
	 // Clock in an employee
    @PostMapping("/clock-in/{employeeId}")
    public ResponseEntity<String> clockIn(@PathVariable long employeeId) {
        attendanceService.clockIn(employeeId);
        return ResponseEntity.ok("Employee clocked in successfully.");
    }

    // Clock out an employee
    @PostMapping("/clock-out/{employeeId}")
    public ResponseEntity<String> clockOut(@PathVariable long employeeId) {
        try {
            attendanceService.clockOut(employeeId);
            return ResponseEntity.ok("Employee clocked out successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Get attendance records for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Attendance>> getAttendanceByEmployee(@PathVariable long employeeId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByEmployee(employeeId));
    }
    
 // Get attendance by a specific date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
    }

    // Calculate work hours for an attendance record
    @GetMapping("/hours/{attendanceId}")
    public ResponseEntity<Double> calculateWorkHours(@PathVariable long attendanceId) {
        try {
            return ResponseEntity.ok(attendanceService.calculateWorkHours(attendanceId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}