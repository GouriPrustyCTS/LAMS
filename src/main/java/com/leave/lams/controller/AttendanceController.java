package com.leave.lams.controller;

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

}