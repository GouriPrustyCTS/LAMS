package com.leave.lams.service;

import java.util.List;
import java.util.Optional;

import com.leave.lams.model.ShiftSwapRequest;

public interface ShiftSwapRequestService {
    public ShiftSwapRequest createRequest(ShiftSwapRequest request);
    public List<ShiftSwapRequest> getAllRequests();
    public Optional<ShiftSwapRequest> getRequestById(Long id);
    public List<ShiftSwapRequest> getPendingRequests();
    public ShiftSwapRequest updateRequestStatus(Long id, String status);
}
