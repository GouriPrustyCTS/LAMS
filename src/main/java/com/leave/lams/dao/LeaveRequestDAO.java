package com.leave.lams.dao;


import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
	    	Optional<LeaveBalance> balance = leaveBalanceRepository
	    		    .findByEmployeeEmployeeIdAndLeaveType(leaveRequest.getEmployee().getEmployeeId(), leaveRequest.getLeaveType());
	    		if (balance == null || balance.get().getBalance() <= 0) {
	    		    throw new RuntimeException("Insufficient leave balance");
	    		}

	        return leaveRequestRepository.save(leaveRequest);
	    }

	    public List<LeaveRequest> getAllLeaveRequests() {
	        return leaveRequestRepository.findAll();
	    }

	    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
	        return leaveRequestRepository.findById(id);
	    }

	    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest updatedRequest) {
	        Optional<LeaveRequest> existing = leaveRequestRepository.findById(id);
			if (existing.isPresent()) {
				LeaveRequest r = existing.get();
				
				if(!r.getEmployee().getEmployeeId().equals(updatedRequest.getEmployee().getEmployeeId())) {
					throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
				}
				
				updatedRequest.setLeaveRequestId(id);
				return leaveRequestRepository.save(updatedRequest);
			}
			return null;
	    }
	    
	    @Override
	    public LeaveRequest updateLeaveStatus(Long id, String newStatus) {
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
	            return leaveRequestRepository.save(request);
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
	    
	    public List<LeaveRequest> getLeaveReportDataSortedByMonth() {
	        List<LeaveRequest> allLeaves = leaveRequestRepository.findAllOrderByStartDateMonthAscAndStartDateAsc();
	        return allLeaves;
	    }


	    public List<LeaveRequest> getLeaveDetailsByEmployee(Long empId) {
	        List<LeaveRequest> employeeLeaves = leaveRequestRepository.findByEmployee_EmployeeId(empId);
	        return employeeLeaves;
	    }
}
