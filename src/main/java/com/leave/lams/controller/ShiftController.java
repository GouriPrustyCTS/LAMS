package com.leave.lams.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.service.ShiftService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/shift")
public class ShiftController {

	private static final Logger logger = LoggerFactory.getLogger(ShiftController.class);

	@Autowired
	private ShiftService shiftService;

	@PostMapping("/add")
	public ShiftDTO createShift(@Valid @RequestBody ShiftDTO shift) {
		logger.info("Request received: POST /shift/add - Request Body: {}", shift);
		ShiftDTO createdShift = shiftService.createShift(shift);
		logger.info("Response sent: POST /shift/add - Shift created with ID: {}", createdShift.getShiftId());
		return createdShift;
	}

	@GetMapping("/")
	public List<ShiftDTO> getAllShifts() {
		logger.info("Request received: GET /shift/");
		List<ShiftDTO> shifts = shiftService.getAllShifts();
		logger.info("Response sent: GET /shift/ - Retrieved {} shifts", shifts.size());
		return shifts;
	}

	@GetMapping("/{shiftID}")
	public ResponseEntity<ShiftDTO> getShiftById(@PathVariable Long shiftID) {
		logger.info("Request received: GET /shift/{}", shiftID);
		ResponseEntity<ShiftDTO> response = shiftService.getShiftById(shiftID)
				.map(ResponseEntity::ok).orElseGet(() -> {
					logger.warn("Response sent: GET /shift/{} - Shift not found", shiftID);
					return ResponseEntity.notFound().build();
				});
		logger.info("Response sent: GET /shift/{}", shiftID);
		return response;
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ShiftDTO update(@PathVariable Long id,@Valid @RequestBody ShiftDTO shift) {
		logger.info("Request received: PUT /shift/{} - Request Body: {}", id, shift);
		ShiftDTO updatedShift = shiftService.updateShift(id, shift);
		logger.info("Response sent: PUT /shift/{} - Shift updated", id);
		return updatedShift;
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable Long id) {
		logger.info("Request received: DELETE /shift/{}", id);
		shiftService.deleteShift(id);
		logger.info("Response sent: DELETE /shift/{} - Shift deleted", id);
	}
}
