package com.leave.lams.service;

import com.leave.lams.model.LeaveRequest;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {

	// In the implementation, log the start and end of this method.
	public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);

	// In the implementation, log the start and end of this method.
	public List<LeaveRequest> getAllLeaveRequests();

	// In the implementation, log the start and end of this method.
	public Optional<LeaveRequest> getLeaveRequestById(Long id);

	// In the implementation, log the start and end of this method.
	public LeaveRequest updateLeaveRequest(Long id, LeaveRequest updatedRequest);

	// In the implementation, log the start and end of this method.
	public boolean deleteLeaveRequest(Long id);

	// In the implementation, log the start and end of this method.
	public LeaveRequest updateLeaveStatus(Long id, String newStatus);

}
