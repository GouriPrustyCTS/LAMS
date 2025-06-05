package com.leave.lams.service;


import java.util.List;
import java.util.Optional;

import com.leave.lams.dto.LeaveBalanceDTO;

public interface LeaveBalanceService {
    public LeaveBalanceDTO createLeaveBalance(LeaveBalanceDTO leaveBalance) ;

    public List<LeaveBalanceDTO> getAllLeaveBalances() ;

    public Optional<LeaveBalanceDTO> getLeaveBalanceById(Long employeeID) ;
    public LeaveBalanceDTO updateLeaveBalanceByLeaveId(Long leaveId, LeaveBalanceDTO leaveBalance);

    public List<LeaveBalanceDTO> updateLeaveBalancesByEmployeeId(Long employeeId, LeaveBalanceDTO leaveBalance);
    public void deleteLeaveBalance(Long id);

    public boolean checkEmployeeExists(Long employeeId);
}
