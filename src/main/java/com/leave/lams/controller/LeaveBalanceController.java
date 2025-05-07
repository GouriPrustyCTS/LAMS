package com.leave.lams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.leave.lams.model.LeaveBalance;
import com.leave.lams.repository.LeaveBalanceRepository;
import com.leave.lams.service.LeaveBalanceService;

import java.util.List;

@RestController
@RequestMapping("/leaveBalances")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @PostMapping("/add")
    public LeaveBalance createLeaveBalance(@RequestBody LeaveBalance leaveBalance) {
        return leaveBalanceService.createLeaveBalance(leaveBalance);
    }

    @GetMapping("/")
    public List<LeaveBalance> getAllLeaveBalances() {
        return leaveBalanceService.getAllLeaveBalances();
    }

    @GetMapping("/{employeeID}")
    public ResponseEntity<LeaveBalance> getLeaveBalanceById(@PathVariable Long employeeID) {
        return leaveBalanceService.getLeaveBalanceById(employeeID)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public LeaveBalance update(@PathVariable Long id, @RequestBody LeaveBalance leaveBalance) {
    	return leaveBalanceService.updateLeaveBalance(id, leaveBalance);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	leaveBalanceService.deLeaveBalance(id);
    }
}

