package com.leave.lams.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.Attendance;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
