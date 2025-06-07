package com.leave.lams.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
	public List<LeaveRequest> findByEmployeeEmployeeId(Long employeeId);

	@Query("SELECT e FROM LeaveRequest e ORDER BY FUNCTION('MONTH', e.startDate) ASC, e.startDate ASC")
	public List<LeaveRequest> findAllOrderByStartDateMonthAscAndStartDateAsc();

	public List<LeaveRequest> findByEmployee_EmployeeId(Long empId);

	@Query("SELECT e.reason AS reason, COUNT(e) AS count FROM LeaveRequest e GROUP BY e.reason")
	public List<Map<String, Object>> countLeaveReasons();

	public List<LeaveRequest> findAll();

	public List<LeaveRequest> findByStatus(String string);

}
