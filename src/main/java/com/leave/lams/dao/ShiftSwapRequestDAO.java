package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.model.ShiftSwapRequest;
import com.leave.lams.repository.ShiftRepository;
import com.leave.lams.repository.ShiftSwapRequestRepository;
import com.leave.lams.service.ShiftSwapRequestService;

@Service
public class ShiftSwapRequestDAO implements ShiftSwapRequestService {

    @Autowired
    private ShiftSwapRequestRepository shiftSwapRequestRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    public ShiftSwapRequest createRequest(ShiftSwapRequest request) {
        request.setStatus("PENDING");
        return shiftSwapRequestRepository.save(request);
    }

    public List<ShiftSwapRequest> getAllRequests() {
        return shiftSwapRequestRepository.findAll();
    }

    public Optional<ShiftSwapRequest> getRequestById(Long id) {
        return shiftSwapRequestRepository.findById(id);
    }

    public List<ShiftSwapRequest> getPendingRequests() {
        return shiftSwapRequestRepository.findByStatus("PENDING");
    }

    public ShiftSwapRequest updateRequestStatus(Long id, String status) {
        return shiftSwapRequestRepository.findById(id).map(req -> {
            req.setStatus(status);
            if ("APPROVED".equalsIgnoreCase(status)) {
                // Perform the actual swap
                Shift shift1 = req.getFromShift();
                Shift shift2 = req.getToShift();
                Employee temp = shift1.getEmployee();
                shift1.setEmployee(shift2.getEmployee());
                shift2.setEmployee(temp);
                shiftRepository.save(shift1);
                shiftRepository.save(shift2);
            }
            return shiftSwapRequestRepository.save(req);
        }).orElseThrow(() -> new IllegalArgumentException("Swap request not found with ID: " + id));
    }
}
