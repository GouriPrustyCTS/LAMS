package com.leave.lams.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dto.LeaveRequestDTO;
import com.leave.lams.mapper.LeaveRequestMapper;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveRequestRepository;
import com.leave.lams.service.LeaveRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/leaveRequests")
public class LeaveRequestController {

	private static final Logger logger = LoggerFactory.getLogger(LeaveRequestController.class);

	@Autowired
	private LeaveRequestService leaveRequestService;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;
	
	@Autowired
	private LeaveRequestMapper mapper;

	@PostMapping("/add")
	public LeaveRequestDTO createLeaveRequest(@Valid @RequestBody LeaveRequestDTO leaveRequest) {
		logger.info("Request received: POST /leaveRequests/add - Request Body: {}", leaveRequest);
		LeaveRequestDTO createdRequest = leaveRequestService.createLeaveRequest(leaveRequest);
		if (createdRequest != null) {
			logger.info("Response sent: POST /leaveRequests/add - LeaveRequest created with ID: {}", createdRequest.getLeaveRequestId());
		} else {
			logger.warn("Response sent: POST /leaveRequests/add - LeaveRequest add failed.");
		}
		return createdRequest;
	}

	@GetMapping("/")
	public List<LeaveRequestDTO> getAllLeaveRequests() {
		logger.info("Request received: GET /leaveRequests/");
		List<LeaveRequestDTO> leaveRequests = leaveRequestService.getAllLeaveRequests();
		logger.info("Response sent: GET /leaveRequests/ - Retrieved {} leave requests", leaveRequests.size());
		return leaveRequests;
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveRequestDTO> getLeaveRequestById(@PathVariable Long id) {
		logger.info("Request received: GET /leaveRequests/{}", id);
		Optional<LeaveRequestDTO> leaveRequest = leaveRequestService.getLeaveRequestById(id);
		if (leaveRequest.isPresent()) {
			logger.info("Response sent: GET /leaveRequests/{} - LeaveRequest found", id);
			return ResponseEntity.ok(leaveRequest.get());
		} else {
			logger.warn("Response sent: GET /leaveRequests/{} - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<LeaveRequestDTO> updateLeaveRequest(@PathVariable Long id,@Valid @RequestBody LeaveRequestDTO updatedRequest) {
		logger.info("Request received: PUT /leaveRequests/{} - Request Body: {}", id, updatedRequest);
		LeaveRequestDTO leaveRequest = leaveRequestService.updateLeaveRequest(id, updatedRequest);
		if (leaveRequest == null) {
			logger.warn("Response sent: PUT /leaveRequests/{} - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
		logger.info("Response sent: PUT /leaveRequests/{} - LeaveRequest updated", id);
		return ResponseEntity.ok(leaveRequest);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
		logger.info("Request received: DELETE /leaveRequests/{}", id);
		boolean res = leaveRequestService.deleteLeaveRequest(id);
		if (!res) {
			logger.warn("Response sent: DELETE /leaveRequests/{} - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
		logger.info("Response sent: DELETE /leaveRequests/{} - LeaveRequest deleted", id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<LeaveRequestDTO> updateLeaveStatus(@PathVariable Long id,@Valid @RequestBody Map<String, String> body) {
		logger.info("Request received: PATCH /leaveRequests/{}/status - Request Body: {}", id, body);
		String newStatus = body.get("status");
		LeaveRequestDTO updated = leaveRequestService.updateLeaveStatus(id, newStatus);
		if (updated == null) {
			logger.warn("Response sent: PATCH /leaveRequests/{}/status - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
		logger.info("Response sent: PATCH /leaveRequests/{}/status - LeaveRequest status updated to {}", id, newStatus);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/employee/{employeeId}")
	public List<LeaveRequestDTO> getRequestsByEmployee(@PathVariable Long employeeId) {
		logger.info("Request received: GET /leaveRequests/employee/{}", employeeId);
		List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeEmployeeId(employeeId);
		logger.info("Response sent: GET /leaveRequests/employee/{} - Retrieved {} leave requests", employeeId, leaveRequests.size());
		return leaveRequests.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}
	
//	_____________________________________________________________________________________________________________________________________
	
	
}
