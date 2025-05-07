package com.leave.lams.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.model.LeaveBalance;
import com.leave.lams.service.LeaveBalanceService;

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

