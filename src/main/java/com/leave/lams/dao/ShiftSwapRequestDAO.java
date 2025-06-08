package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.ShiftSwapRequestDTO;
import com.leave.lams.exception.ShiftSwapRequestNotFoundException;
import com.leave.lams.mapper.ShiftSwapRequestMapper;
import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.model.ShiftSwapRequest;
import com.leave.lams.repository.ShiftRepository;
import com.leave.lams.repository.ShiftSwapRequestRepository;
import com.leave.lams.service.ShiftSwapRequestService;

@Service
public class ShiftSwapRequestDAO implements ShiftSwapRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ShiftSwapRequestDAO.class);

    @Autowired
    private ShiftSwapRequestRepository shiftSwapRequestRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftSwapRequestMapper mapper;

    @Override
    public ShiftSwapRequestDTO createRequest(ShiftSwapRequestDTO request) {
        try {
            if (request.getStatus() == null || request.getStatus().isEmpty()) {
                request.setStatus("PENDING");
            }
            ShiftSwapRequest entity = mapper.toEntity(request);
            ShiftSwapRequest saved = shiftSwapRequestRepository.save(entity);
            return mapper.toDto(saved);
        } catch (Exception e) {
            logger.error("Failed to create shift swap request: {}", e.getMessage());
            throw new RuntimeException("Error while creating shift swap request", e);
        }
    }

    @Override
    public List<ShiftSwapRequestDTO> getAllRequests() {
        try {
            List<ShiftSwapRequest> requests = shiftSwapRequestRepository.findAll();
            return requests.stream().map(mapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to fetch all shift swap requests: {}", e.getMessage());
            throw new RuntimeException("Error while fetching shift swap requests", e);
        }
    }

    @Override
    public Optional<ShiftSwapRequestDTO> getRequestById(Long id) {
        try {
            ShiftSwapRequest request = shiftSwapRequestRepository.findById(id)
                .orElseThrow(() -> new ShiftSwapRequestNotFoundException("Swap request not found with ID: " + id));
            return Optional.of(mapper.toDto(request));
        } catch (ShiftSwapRequestNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching shift swap request with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error while fetching request by ID", e);
        }
    }

    // REMOVED: getPendingRequests() method. Its functionality is now part of getRequestsByStatus("PENDING")

    // NEW: Implementation for getting requests filtered by status
    @Override
    public List<ShiftSwapRequestDTO> getRequestsByStatus(String status) {
        try {
            List<ShiftSwapRequest> requests = shiftSwapRequestRepository.findByStatus(status);
            return requests.stream().map(mapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching shift swap requests by status {}: {}", status, e.getMessage());
            throw new RuntimeException("Error while fetching requests by status", e);
        }
    }

    @Override
    public ShiftSwapRequestDTO updateRequestStatus(Long id, String status) {
        try {
            ShiftSwapRequest request = shiftSwapRequestRepository.findById(id)
                .orElseThrow(() -> new ShiftSwapRequestNotFoundException("Swap request not found with ID: " + id));

            request.setStatus(status);

            if ("APPROVED".equalsIgnoreCase(status)) {
                Shift shift1 = request.getFromShift();
                Shift shift2 = request.getToShift();

                if (shift1 == null || shift2 == null) {
                    throw new RuntimeException("One or both shifts associated with the swap request are missing. Cannot approve.");
                }

                // Perform swap logic: exchange employees assigned to the shifts
                Employee tempEmployee = shift1.getEmployee();
                shift1.setEmployee(shift2.getEmployee());
                shift2.setEmployee(tempEmployee);

                shiftRepository.save(shift1);
                shiftRepository.save(shift2);
            }

            ShiftSwapRequest saved = shiftSwapRequestRepository.save(request);
            return mapper.toDto(saved);

        } catch (ShiftSwapRequestNotFoundException e) {
            logger.warn("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating request status for ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update request status", e);
        }
    }
}