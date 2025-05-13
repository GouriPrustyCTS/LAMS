package com.leave.lams.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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




	@Query("SELECT t FROM Attendance t WHERE t.employee.employeeId = :employeeId ORDER BY t.attendanceDate")
	List<Attendance> findByEmployee_EmployeeIdOrderByDate(@Param("employeeId") Long empId);

	//  Fetch data for a specific date range
	@Query("SELECT t FROM Attendance t WHERE t.employee.employeeId = :employeeId AND t.attendanceDate BETWEEN :startDate AND :endDate ORDER BY t.attendanceDate")
	List<Attendance> findByEmployee_EmployeeIdAndDateBetweenOrderByDate(@Param("employeeId") Long empId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("SELECT t.employee.employeeId AS empId, t.employee.name AS empName, t.clockInTime AS clockInTime, t.clockOutTime AS clockOutTime, t.attendanceDate AS date FROM Attendance t")
	List<Map<String, Object>> getClockInOutData();

	@Query("SELECT t.employee.employeeId AS empId, t.employee.name AS empName, t.clockInTime AS clockInTime, t.clockOutTime AS clockOutTime, t.attendanceDate AS date FROM Attendance t WHERE t.employee.employeeId = :empId")
	List<Map<String, Object>> getClockInOutDataByEmpId(@Param("empId") Long empId);


}