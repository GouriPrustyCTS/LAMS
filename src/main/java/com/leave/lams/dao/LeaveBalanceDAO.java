package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;
import com.leave.lams.service.LeaveBalanceService;

import jakarta.transaction.Transactional;

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
	@Transactional
	public LeaveBalance updateLeaveBalance(Long id, LeaveBalance leaveBalance) {
	    return leaveBalanceRepository.findById(id)
	        .map(existing -> {
	            if (!existing.getEmployee().getEmployeeId().equals(leaveBalance.getEmployee().getEmployeeId())) {
	                throw new IllegalArgumentException("Employee ID mismatch.");
	            }
	            existing.setLeaveType(leaveBalance.getLeaveType());
	            existing.setBalance(leaveBalance.getBalance());
	            return leaveBalanceRepository.save(existing);
	        })
	        .orElseThrow(() -> new IllegalArgumentException("LeaveBalance not found with ID: " + id));
	}


	@Override
	public void deleteLeaveBalance(Long id) {
		leaveBalanceRepository.deleteById(id);
	}
}
