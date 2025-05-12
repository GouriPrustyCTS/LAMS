package com.leave.lams.service;
import com.leave.lams.model.LeaveRequest;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestService {


    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) ;

    public List<LeaveRequest> getAllLeaveRequests() ;

    public Optional<LeaveRequest> getLeaveRequestById(Long id);

    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest updatedRequest) ;

    public boolean deleteLeaveRequest(Long id) ;
    
    public LeaveRequest updateLeaveStatus(Long id, String newStatus);

}
