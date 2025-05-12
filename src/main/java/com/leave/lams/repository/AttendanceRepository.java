package com.leave.lams.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.Attendance;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	// Finds all attendance records for a given employee ID.
	List<Attendance> findByEmployee_EmployeeId(Long employeeId);
	
	// Finds all attendance records for a specific date.
	List<Attendance> findByAttendanceDate(LocalDate date);

	// Finds the latest attendance record for a given employee ID, ordered by clock-in time in descending order.
	@Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId ORDER BY a.clockInTime DESC")
	List<Attendance> findLatestByEmployeeId(@Param("employeeId") Long employeeId);

	// Finds an attendance record by its ID.
	Optional<Attendance> findByAttendanceId(Long attendanceId);
}
