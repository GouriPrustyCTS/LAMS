package com.leave.lams.dao;


import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.LeaveRequestDTO;
import com.leave.lams.mapper.LeaveRequestMapper;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveBalanceRepository;
import com.leave.lams.repository.LeaveRequestRepository;
import com.leave.lams.service.LeaveRequestService;

@Service
public class LeaveRequestDAO implements LeaveRequestService{

	    @Autowired
	    private LeaveRequestRepository leaveRequestRepository;
	    
	    @Autowired
	    private LeaveBalanceRepository leaveBalanceRepository;
	    
	    @Autowired
	    private LeaveRequestMapper mapper;

	    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequest) {
	    	Optional<LeaveBalance> balance = leaveBalanceRepository
	    		    .findByEmployeeEmployeeIdAndLeaveType(leaveRequest.getEmployeeId(), leaveRequest.getLeaveType());
	    		if (balance == null || balance.get().getBalance() <= 0) {
	    		    throw new RuntimeException("Insufficient leave balance");
	    		}

	        return mapper.toDTo(leaveRequestRepository.save(mapper.toEntity(leaveRequest)));
	    }

	    public List<LeaveRequestDTO> getAllLeaveRequests() {
			List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();
			return leaveRequests.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	    }

	    public Optional<LeaveRequestDTO> getLeaveRequestById(Long id) {
			Optional<LeaveRequest> shift = leaveRequestRepository.findById(id);
		    if (shift.isPresent()) {
		        return Optional.of(mapper.toDTo(shift.get()));
		    }
		    return Optional.empty();
	    }

	    public LeaveRequestDTO updateLeaveRequest(Long id, LeaveRequestDTO updatedRequest) {
	        Optional<LeaveRequest> existing = leaveRequestRepository.findById(id);
			if (existing.isPresent()) {
				LeaveRequest r = existing.get();
				
				if(!r.getEmployee().getEmployeeId().equals(updatedRequest.getEmployeeId())) {
					throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
				}
				
				updatedRequest.setLeaveRequestId(id);
				return mapper.toDTo(leaveRequestRepository.save(mapper.toEntity(updatedRequest)));
			}
			return null;
	    }
	    
	    @Override
	    public LeaveRequestDTO updateLeaveStatus(Long id, String newStatus) {
	        Optional<LeaveRequest> optional = leaveRequestRepository.findById(id);
	        if (optional.isPresent()) {
	            LeaveRequest request = optional.get();
	            
	            
	            if (request.getStatus().equals("APPROVED")) {
	                long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
	                LeaveBalance balance = leaveBalanceRepository
	                    .findByEmployeeEmployeeIdAndLeaveType(request.getEmployee().getEmployeeId(), request.getLeaveType())
	                    .orElseThrow(() -> new RuntimeException("No balance record found"));

	                balance.setBalance(balance.getBalance() - days);
	                leaveBalanceRepository.save(balance);
	            }

	            
	            
	            request.setStatus(newStatus);
	            return  mapper.toDTo(leaveRequestRepository.save(request));
	        }
	        return null;
	    }


	    public boolean deleteLeaveRequest(Long id) {
	        if (leaveRequestRepository.existsById(id)) {
	            leaveRequestRepository.deleteById(id);
	            return true;
	        }
	        return false;
	    }
	    
	    public List<LeaveRequestDTO> getLeaveReportDataSortedByMonth() {
	        List<LeaveRequest> allLeaves = leaveRequestRepository.findAllOrderByStartDateMonthAscAndStartDateAsc();
	        return allLeaves.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	    }


	    public List<LeaveRequestDTO> getLeaveDetailsByEmployee(Long empId) {
	        List<LeaveRequest> employeeLeaves = leaveRequestRepository.findByEmployee_EmployeeId(empId);
	        return employeeLeaves.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	    }
}
