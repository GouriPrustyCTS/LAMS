package com.leave.lams.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dto.ShiftSwapRequestDTO;
import com.leave.lams.model.ShiftSwapRequest;
import com.leave.lams.service.ShiftSwapRequestService;

import jakarta.validation.Valid;

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

    @GetMapping("/")
    public List<ShiftSwapRequestDTO> getAllRequests() {
        logger.info("GET /swap/ - Fetching all swap requests");
        return shiftSwapRequestService.getAllRequests();
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

    @GetMapping("/pending")
    public List<ShiftSwapRequestDTO> getPendingRequests() {
        logger.info("GET /swap/pending - Fetching pending swap requests");
        return shiftSwapRequestService.getPendingRequests();
    }

    @PutMapping("/{id}/status") // swap/status?status=APPROVED
    public ShiftSwapRequestDTO updateStatus(@PathVariable Long id, @RequestParam String status) {
        logger.info("PUT /swap/{}/status - Updating to {}", id, status);
        return shiftSwapRequestService.updateRequestStatus(id, status);
    }
}

