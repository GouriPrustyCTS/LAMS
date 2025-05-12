package com.leave.lams.service;


import com.leave.lams.model.LeaveBalance;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceService {
    public LeaveBalance createLeaveBalance(LeaveBalance leaveBalance) ;

    public List<LeaveBalance> getAllLeaveBalances() ;

    public Optional<LeaveBalance> getLeaveBalanceById(Long employeeID) ;
    
    public LeaveBalance updateLeaveBalance(Long id, LeaveBalance leaveBalance);
    
    public void deleteLeaveBalance(Long id);
}
