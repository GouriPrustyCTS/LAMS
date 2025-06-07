package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.LeaveBalanceDTO;
import com.leave.lams.exception.LeaveBalanceNotFoundException;
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

	@Transactional
	public LeaveBalanceDTO createLeaveBalance(LeaveBalanceDTO leaveBalance) {
		LeaveBalance leave = mapper.toEntity(leaveBalance);
		LeaveBalance savedLeave = leaveBalanceRepository.save(leave);
		return mapper.toDTo(savedLeave);
	}

	public List<LeaveBalanceDTO> getAllLeaveBalances() {
		List<LeaveBalance> leaves = leaveBalanceRepository.findAll();
		return leaves.stream().map(mapper::toDTo).collect(Collectors.toList());
	}

	public Optional<LeaveBalanceDTO> getLeaveBalanceById(Long employeeID) {
		LeaveBalance leave = leaveBalanceRepository.findById(employeeID)
				.orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found for ID: " + employeeID));
		return Optional.of(mapper.toDTo(leave));
	}

	@Override
    @Transactional
    public LeaveBalanceDTO updateLeaveBalanceByLeaveId(Long leaveId, LeaveBalanceDTO leaveBalance) {
        LeaveBalance existing = leaveBalanceRepository.findById(leaveId)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found with ID: " + leaveId));

        existing.setLeaveType(leaveBalance.getLeaveType());
        existing.setBalance(leaveBalance.getBalance());

        return mapper.toDTo(leaveBalanceRepository.save(existing));
    }

    @Override
    @Transactional
    public List<LeaveBalanceDTO> updateLeaveBalancesByEmployeeId(Long employeeId, LeaveBalanceDTO leaveBalance) {
    	Optional<List<LeaveBalance>> existingLeaveBalances = leaveBalanceRepository.findAllByEmployee_EmployeeId(employeeId);
        if (existingLeaveBalances.isEmpty() || existingLeaveBalances.get().isEmpty()) {
            throw new LeaveBalanceNotFoundException("No leave balances found for Employee ID: " + employeeId);
        }
    
        existingLeaveBalances.get().forEach(existing -> {
        	if(leaveBalance.getLeaveType().equals(existing.getLeaveType())) {
        		existing.setBalance(leaveBalance.getBalance());
        	}
        });
    
        List<LeaveBalance> updatedLeaveBalances = leaveBalanceRepository.saveAll(existingLeaveBalances.get());
        return updatedLeaveBalances.stream().map(mapper::toDTo).collect(Collectors.toList());
    }

    @Transactional
	@Override
	public void deleteLeaveBalance(Long id) {
		if (!leaveBalanceRepository.existsById(id)) {
			throw new LeaveBalanceNotFoundException("Cannot delete. Leave balance not found for ID: " + id);
		}
		leaveBalanceRepository.deleteById(id);
	}

//    


	@Override
	public boolean checkEmployeeExists(Long employeeId) {
		return leaveBalanceRepository.existsByEmployee_EmployeeId(employeeId); // Assuming existsByEmployeeId is defined
																				// in LeaveBalanceRepository
	}
//    
	
    @Override
    public Optional<List<LeaveBalanceDTO>> getLeaveBalancesByEmployeeId(Long employeeId) {
        Optional<List<LeaveBalance>> optionalLeaveList = leaveBalanceRepository.findAllByEmployee_EmployeeId(employeeId);
        if (optionalLeaveList.isEmpty() || optionalLeaveList.get().isEmpty()) {
            return Optional.empty();
        }
        List<LeaveBalance> leaveList = optionalLeaveList.get();
        List<LeaveBalanceDTO> dtoList = leaveList.stream()
                .map(mapper::toDTo)
                .collect(Collectors.toList());
        return Optional.of(dtoList);
    }

	
}
