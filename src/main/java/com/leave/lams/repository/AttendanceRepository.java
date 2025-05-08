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
	List<Attendance> findByEmployeeId(Long employeeId); // Changed method reference
    List<Attendance> findByAttendanceDate(LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId ORDER BY a.clockInTime DESC")
    List<Attendance> findLatestByEmployeeId(@Param("employeeId") Long employeeId);

    Optional<Attendance> findByAttendanceId(Long attendanceId);
}
