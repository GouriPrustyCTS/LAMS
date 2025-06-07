package com.leave.lams.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.leave.lams.dto.LeaveRequestDTO;
@Service
public interface LeaveRequestService {

	// In the implementation, log the start and end of this method.
	public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequest);

	// In the implementation, log the start and end of this method.
	public List<LeaveRequestDTO> getAllLeaveRequests();

	// In the implementation, log the start and end of this method.
	public Optional<LeaveRequestDTO> getLeaveRequestById(Long id);

	// In the implementation, log the start and end of this method.
	public LeaveRequestDTO updateLeaveRequest(Long id, LeaveRequestDTO updatedRequest);

	// In the implementation, log the start and end of this method.
	public boolean deleteLeaveRequest(Long id);

	// In the implementation, log the start and end of this method.
	public LeaveRequestDTO updateLeaveStatus(Long id, String newStatus);
	
	public List<LeaveRequestDTO> getLeaveReportDataSortedByMonth();

    public List<LeaveRequestDTO> getLeaveDetailsByEmployee(Long empId);

	public List<LeaveRequestDTO> getPendingRequests();

}
