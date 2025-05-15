package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.ShiftSwapRequestDTO;
import com.leave.lams.mapper.ShiftMapper;
import com.leave.lams.mapper.ShiftSwapRequestMapper;
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
    
    @Autowired
	private ShiftSwapRequestMapper mapper;

    public ShiftSwapRequestDTO createRequest(ShiftSwapRequestDTO request) {
        request.setStatus("PENDING");
        ShiftSwapRequest shitRequest = mapper.toEntity(request);
        ShiftSwapRequest savedShiftRequest = shiftSwapRequestRepository.save(shitRequest);
        ShiftSwapRequestDTO dtoRes = mapper.toDTo(savedShiftRequest);
        return dtoRes;
    }

    public List<ShiftSwapRequestDTO> getAllRequests() {
        List<ShiftSwapRequest> shiftSwapRequests = shiftSwapRequestRepository.findAll();
		return shiftSwapRequests.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
    }

    public Optional<ShiftSwapRequestDTO> getRequestById(Long id) {
		Optional<ShiftSwapRequest> shitRequest = shiftSwapRequestRepository.findById(id);
	    if (shitRequest.isPresent()) {
	        return Optional.of(mapper.toDTo(shitRequest.get()));
	    }
	    return Optional.empty();
    }

    public List<ShiftSwapRequestDTO> getPendingRequests() {
    	List<ShiftSwapRequest> shitRequest = shiftSwapRequestRepository.findByStatus("PENDING");
        return shitRequest.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
    }

    public ShiftSwapRequestDTO updateRequestStatus(Long id, String status) {
        Optional<ShiftSwapRequest> optionalRequest = shiftSwapRequestRepository.findById(id);

        if (optionalRequest.isEmpty()) {
            throw new IllegalArgumentException("Swap request not found with ID: " + id);
        }

        ShiftSwapRequest request = optionalRequest.get();
        request.setStatus(status);

        if ("APPROVED".equalsIgnoreCase(status)) {
            Shift shift1 = request.getFromShift();
            Shift shift2 = request.getToShift();

            // Perform swap
            Employee temp = shift1.getEmployee();
            shift1.setEmployee(shift2.getEmployee());
            shift2.setEmployee(temp);

            // Save changes
            shiftRepository.save(shift1);
            shiftRepository.save(shift2);
        }
        
        ShiftSwapRequest savedShiftRequest = shiftSwapRequestRepository.save(request);
        ShiftSwapRequestDTO dtoRes = mapper.toDTo(savedShiftRequest);

        return dtoRes;
    }

}

