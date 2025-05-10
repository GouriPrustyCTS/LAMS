package com.leave.lams.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;
import com.leave.lams.service.LeaveBalanceService;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveBalanceDAO implements LeaveBalanceService {

	@Autowired
	private LeaveBalanceRepository leaveBalanceRepository;

	public LeaveBalance createLeaveBalance(LeaveBalance leaveBalance) {
		return leaveBalanceRepository.save(leaveBalance);
	}

	public List<LeaveBalance> getAllLeaveBalances() {
		return leaveBalanceRepository.findAll();
	}

	public Optional<LeaveBalance> getLeaveBalanceById(Long employeeID) {
		return leaveBalanceRepository.findById(employeeID);
	}

	@Override
	public LeaveBalance updateLeaveBalance(Long id, LeaveBalance leaveBalance) {
		Optional<LeaveBalance> existingOptional = leaveBalanceRepository.findById(id);
		if (existingOptional.isPresent()) {
			LeaveBalance lb = existingOptional.get();
			
			if(!lb.getEmployee().getEmployeeId().equals(leaveBalance.getEmployee().getEmployeeId())) {
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}
			
			lb.setLeaveType(leaveBalance.getLeaveType());
			lb.setBalance(leaveBalance.getBalance());
			return leaveBalanceRepository.save(lb);
		}
		return null;
	}

	@Override
	public void deLeaveBalance(Long id) {
		leaveBalanceRepository.deleteById(id);
	}
}
