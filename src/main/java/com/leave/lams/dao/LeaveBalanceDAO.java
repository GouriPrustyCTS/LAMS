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
    public LeaveBalanceDTO updateLeaveBalance(Long id, LeaveBalanceDTO leaveBalance) {
        LeaveBalance existing = leaveBalanceRepository.findById(id)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found with ID: " + id));

        if (!existing.getEmployee().getEmployeeId().equals(leaveBalance.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID mismatch.");
        }

        existing.setLeaveType(leaveBalance.getLeaveType());
        existing.setBalance(leaveBalance.getBalance());

        return mapper.toDTo(leaveBalanceRepository.save(existing));
    }

    @Override
    public void deleteLeaveBalance(Long id) {
        if (!leaveBalanceRepository.existsById(id)) {
            throw new LeaveBalanceNotFoundException("Cannot delete. Leave balance not found for ID: " + id);
        }
        leaveBalanceRepository.deleteById(id);
    }
}
