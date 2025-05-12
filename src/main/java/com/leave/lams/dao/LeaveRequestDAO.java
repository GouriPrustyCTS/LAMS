package com.leave.lams.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveRequestRepository;
import com.leave.lams.service.LeaveRequestService;

@Service
public class LeaveRequestDAO implements LeaveRequestService{

	    @Autowired
	    private LeaveRequestRepository leaveRequestRepository;

	    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
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
	    
	    

	    public boolean deleteLeaveRequest(Long id) {
	        if (leaveRequestRepository.existsById(id)) {
	            leaveRequestRepository.deleteById(id);
	            return true;
	        }
	        return false;
	    }
}
