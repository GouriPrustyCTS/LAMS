package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.LeaveBalanceDTO;
import com.leave.lams.mapper.LeaveBalanceMapper;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;
import com.leave.lams.service.LeaveBalanceService;

import jakarta.transaction.Transactional;

@Service
public class LeaveBalanceDAO implements LeaveBalanceService {

	@Autowired
	private LeaveBalanceRepository leaveBalanceRepository;

	@Autowired
	private LeaveBalanceMapper mapper;

	public LeaveBalanceDTO createLeaveBalance(LeaveBalanceDTO leaveBalance) {
		LeaveBalance leave = mapper.toEntity(leaveBalance);
		LeaveBalance savedLeave = leaveBalanceRepository.save(leave);
		LeaveBalanceDTO dtoRes = mapper.toDTo(savedLeave);
		return dtoRes;
	}

	public List<LeaveBalanceDTO> getAllLeaveBalances() {
		List<LeaveBalance> leaves = leaveBalanceRepository.findAll();
		return leaves.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	public Optional<LeaveBalanceDTO> getLeaveBalanceById(Long employeeID) {
		Optional<LeaveBalance> leave = leaveBalanceRepository.findById(employeeID);
		if (leave.isPresent()) {
			return Optional.of(mapper.toDTo(leave.get()));
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public LeaveBalanceDTO updateLeaveBalance(Long id, LeaveBalanceDTO leaveBalance) {
		Optional<LeaveBalance> optionalExisting = leaveBalanceRepository.findById(id);

		if (optionalExisting.isEmpty()) {
			throw new IllegalArgumentException("LeaveBalance not found with ID: " + id);
		}

		LeaveBalance existing = optionalExisting.get();

		if (!existing.getEmployee().getEmployeeId().equals(leaveBalance.getEmployeeId())) {
			throw new IllegalArgumentException("Employee ID mismatch.");
		}

		// Updating fields
		existing.setLeaveType(leaveBalance.getLeaveType());
		existing.setBalance(leaveBalance.getBalance());

		return mapper.toDTo(leaveBalanceRepository.save(existing));
	}

	@Override
	public void deleteLeaveBalance(Long id) {
		leaveBalanceRepository.deleteById(id);
	}
}
