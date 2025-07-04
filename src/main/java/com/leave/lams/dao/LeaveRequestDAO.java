package com.leave.lams.dao;


import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.LeaveRequestDTO;
import com.leave.lams.exception.LeaveBalanceNotFoundException;
import com.leave.lams.exception.LeaveRequestNotFoundException;
import com.leave.lams.mapper.LeaveRequestMapper;
import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.model.ShiftSwapRequest;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.repository.LeaveBalanceRepository;
import com.leave.lams.repository.LeaveRequestRepository;
import com.leave.lams.service.LeaveRequestService;
@Service
public class LeaveRequestDAO implements LeaveRequestService {
	

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRequestMapper mapper;

    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDto) {
        // Check leave balance
        Optional<LeaveBalance> balance = leaveBalanceRepository
            .findByEmployeeEmployeeIdAndLeaveType(leaveRequestDto.getEmployeeId(), leaveRequestDto.getLeaveType());

        if (balance.isEmpty() || balance.get().getBalance() <= 0) {
            throw new LeaveBalanceNotFoundException("Insufficient leave balance in the leave type for employee ID : " + leaveRequestDto.getEmployeeId());
        }

        // Map DTO to Entity
        LeaveRequest leaveRequest = mapper.toEntity(leaveRequestDto);

        // Set Employee manually (prevent ModelMapper recursion or wrong mapping)
        Employee emp = employeeRepository.findById(leaveRequestDto.getEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found with ID " + leaveRequestDto.getEmployeeId()));
        leaveRequest.setEmployee(emp);

        // Save and return mapped DTO
        return mapper.toDTo(leaveRequestRepository.save(leaveRequest));
    }


    public List<LeaveRequestDTO> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
        return leaveRequests.stream().map(mapper::toDTo).collect(Collectors.toList());
    }

    public Optional<LeaveRequestDTO> getLeaveRequestById(Long id) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found with ID: " + id));
        return Optional.of(mapper.toDTo(request));
    }

    public LeaveRequestDTO updateLeaveRequest(Long id, LeaveRequestDTO updatedRequest) {
        LeaveRequest existing = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found with ID: " + id));

        if (!existing.getEmployee().getEmployeeId().equals(updatedRequest.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID mismatch.");
        }

        updatedRequest.setLeaveRequestId(id);
        return mapper.toDTo(leaveRequestRepository.save(mapper.toEntity(updatedRequest)));
    }

    @Override
    public LeaveRequestDTO updateLeaveStatus(Long id, String newStatus) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found with ID: " + id));

        if (request.getStatus().equals("PENDING") && newStatus.equals("APPROVED")) {
            long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeEmployeeIdAndLeaveType(request.getEmployee().getEmployeeId(), request.getLeaveType())
                    .orElseThrow(() -> new LeaveBalanceNotFoundException("No balance record found for employee ID: "
                            + request.getEmployee().getEmployeeId() + " and leave type: " + request.getLeaveType()));

            balance.setBalance(balance.getBalance() - days);
            leaveBalanceRepository.save(balance);
        }

        request.setStatus(newStatus);
        return mapper.toDTo(leaveRequestRepository.save(request));
    }

    public boolean deleteLeaveRequest(Long id) {
        if (!leaveRequestRepository.existsById(id)) {
            throw new LeaveRequestNotFoundException("Cannot delete. Leave request not found for ID: " + id);
        }
        leaveRequestRepository.deleteById(id);
        return true;
    }

    public List<LeaveRequestDTO> getLeaveReportDataSortedByMonth() {
        List<LeaveRequest> allLeaves = leaveRequestRepository.findAllOrderByStartDateMonthAscAndStartDateAsc();
        return allLeaves.stream().map(mapper::toDTo).collect(Collectors.toList());
    }

    public List<LeaveRequestDTO> getLeaveDetailsByEmployee(Long empId) {
        List<LeaveRequest> employeeLeaves = leaveRequestRepository.findByEmployee_EmployeeId(empId);
        return employeeLeaves.stream().map(mapper::toDTo).collect(Collectors.toList());
    }


	@Override
	public List<LeaveRequestDTO> getPendingRequests() {
        try {
            List<LeaveRequest> requests = leaveRequestRepository.findByStatus("PENDING");
            return requests.stream().map(mapper::toDTo).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching pending requests", e);
        }
	}
}
