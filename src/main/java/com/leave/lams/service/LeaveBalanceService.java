package com.leave.lams.service;


import java.util.List;
import java.util.Optional;

import com.leave.lams.dto.LeaveBalanceDTO;

public interface LeaveBalanceService {
    public LeaveBalanceDTO createLeaveBalance(LeaveBalanceDTO leaveBalance) ;

    public List<LeaveBalanceDTO> getAllLeaveBalances() ;

    public Optional<LeaveBalanceDTO> getLeaveBalanceById(Long employeeID) ;
    
    public LeaveBalanceDTO updateLeaveBalance(Long id, LeaveBalanceDTO leaveBalance);
    
    public void deleteLeaveBalance(Long id);
}
