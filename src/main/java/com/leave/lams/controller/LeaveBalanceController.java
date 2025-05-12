package com.leave.lams.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(LeaveBalanceController.class);

	@Autowired
	private LeaveBalanceService leaveBalanceService;

	@PostMapping("/add")
	public LeaveBalance createLeaveBalance(@RequestBody LeaveBalance leaveBalance) {
		logger.info("Request received: POST /leaveBalances/add - Request Body: {}", leaveBalance);
		LeaveBalance createdLeaveBalance = leaveBalanceService.createLeaveBalance(leaveBalance);
		logger.info("Response sent: POST /leaveBalances/add - LeaveBalance created with ID: {}", createdLeaveBalance);
		return createdLeaveBalance;
	}

	@GetMapping("/")
	public List<LeaveBalance> getAllLeaveBalances() {
		logger.info("Request received: GET /leaveBalances/");
		List<LeaveBalance> leaveBalances = leaveBalanceService.getAllLeaveBalances();
		logger.info("Response sent: GET /leaveBalances/ - Retrieved {} leave balances", leaveBalances.size());
		return leaveBalances;
	}

	@GetMapping("/{employeeID}")
	public ResponseEntity<LeaveBalance> getLeaveBalanceById(@PathVariable Long employeeID) {
		logger.info("Request received: GET /leaveBalances/{}", employeeID);
		ResponseEntity<LeaveBalance> response = leaveBalanceService.getLeaveBalanceById(employeeID)
				.map(ResponseEntity::ok).orElseGet(() -> {
					logger.warn("Response sent: GET /leaveBalances/{} - LeaveBalance not found", employeeID);
					return ResponseEntity.notFound().build();
				});
		logger.info("Response sent: GET /leaveBalances/{}", employeeID);
		return response;
	}

	@PutMapping("/{id}")
	public LeaveBalance update(@PathVariable Long id, @RequestBody LeaveBalance leaveBalance) {
		logger.info("Request received: PUT /leaveBalances/{} - Request Body: {}", id, leaveBalance);
		LeaveBalance updatedLeaveBalance = leaveBalanceService.updateLeaveBalance(id, leaveBalance);
		logger.info("Response sent: PUT /leaveBalances/{} - LeaveBalance updated", id);
		return updatedLeaveBalance;
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		logger.info("Request received: DELETE /leaveBalances/{}", id);
		leaveBalanceService.deleteLeaveBalance(id);
		logger.info("Response sent: DELETE /leaveBalances/{} - LeaveBalance deleted", id);
	}
}
