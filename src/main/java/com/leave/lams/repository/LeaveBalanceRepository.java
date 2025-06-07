package com.leave.lams.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.LeaveBalance;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
	Optional<LeaveBalance> findByEmployeeEmployeeIdAndLeaveType(Long employeeId, String leaveType);
	
//	
	Optional<LeaveBalance> findByEmployee_EmployeeId(Long employeeId);
    
    public boolean existsByEmployee_EmployeeId(Long employeeId);
//	

    Optional<List<LeaveBalance>> findAllByEmployee_EmployeeId(Long employeeId);
}
