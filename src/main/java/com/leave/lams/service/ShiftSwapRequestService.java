package com.leave.lams.service;

import java.util.List;
import java.util.Optional;

import com.leave.lams.dto.ShiftSwapRequestDTO;

public interface ShiftSwapRequestService {
    public ShiftSwapRequestDTO createRequest(ShiftSwapRequestDTO request);
    public List<ShiftSwapRequestDTO> getAllRequests();
    public Optional<ShiftSwapRequestDTO> getRequestById(Long id);
    public ShiftSwapRequestDTO updateRequestStatus(Long id, String status);
    public List<ShiftSwapRequestDTO> getRequestsByStatus(String status);
}
