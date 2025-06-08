package com.leave.lams.controller;

import com.leave.lams.dto.ShiftSwapRequestDTO;
import com.leave.lams.service.ShiftSwapRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/swap")
public class ShiftSwapRequestController {

    private static final Logger logger = LoggerFactory.getLogger(ShiftSwapRequestController.class);

    @Autowired
    private ShiftSwapRequestService shiftSwapRequestService;

    @PostMapping("/request")
    public ShiftSwapRequestDTO createSwapRequest(@Valid @RequestBody ShiftSwapRequestDTO request) {
        logger.info("POST /swap/request - Request: {}", request);
        ShiftSwapRequestDTO created = shiftSwapRequestService.createRequest(request);
        logger.info("Swap request created with ID: {}", created.getId());
        return created;
    }

    // UPDATED: Now accepts an optional 'status' query parameter
    @GetMapping("/")
    public List<ShiftSwapRequestDTO> getAllRequests(@RequestParam(required = false) String status) {
        logger.info("GET /swap/ - Fetching swap requests with status: {}", status != null ? status : "ALL");
        if (status != null && !status.trim().isEmpty()) {
            return shiftSwapRequestService.getRequestsByStatus(status); // Delegates to a new method in service
        }
        return shiftSwapRequestService.getAllRequests(); // Fetches all if no status specified
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftSwapRequestDTO> getRequestById(@PathVariable Long id) {
        logger.info("GET /swap/{} - Fetching swap request", id);
        return shiftSwapRequestService.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Swap request not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Removed the dedicated @GetMapping("/pending") as it's now handled by @GetMapping("/") with status=PENDING
    // The previous @GetMapping("/pending") method: getPendingRequests() is no longer directly exposed here.
    // Its logic will be part of getRequestsByStatus("PENDING") in the service.

    @PutMapping("/{id}/status") // swap/{id}/status?status=APPROVED
    public ShiftSwapRequestDTO updateStatus(@PathVariable Long id, @RequestParam String status) {
        logger.info("PUT /swap/{}/status - Updating to {}", id, status);
        return shiftSwapRequestService.updateRequestStatus(id, status);
    }
}