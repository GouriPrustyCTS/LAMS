package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.exception.ShiftNotFoundException;
import com.leave.lams.mapper.ShiftMapper;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.ShiftRepository;
import com.leave.lams.service.ShiftService;
@Service
public class ShiftDAO implements ShiftService {

    private static final Logger logger = LoggerFactory.getLogger(ShiftDAO.class);

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftMapper mapper;

    @Override
    public ShiftDTO createShift(ShiftDTO shiftDto) {
        try {
            Shift shift = mapper.toEntity(shiftDto);
            Shift savedShift = shiftRepository.save(shift);
            return mapper.toDTo(savedShift);
        } catch (Exception e) {
            logger.error("Failed to create shift: {}", e.getMessage());
            throw new RuntimeException("Error while creating shift", e);
        }
    }

    @Override
    public List<ShiftDTO> getAllShifts() {
        try {
            List<Shift> shifts = shiftRepository.findAll();
            return shifts.stream().map(mapper::toDTo).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to fetch all shifts: {}", e.getMessage());
            throw new RuntimeException("Error while fetching shifts", e);
        }
    }

    @Override
    public Optional<ShiftDTO> getShiftById(Long shiftID) {
        try {
            Optional<Shift> shiftOpt = shiftRepository.findById(shiftID);
            if (shiftOpt.isPresent()) {
                return Optional.of(mapper.toDTo(shiftOpt.get()));
            } else {
                throw new ShiftNotFoundException("Shift not found with ID: " + shiftID);
            }
        } catch (ShiftNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching shift with ID {}: {}", shiftID, e.getMessage());
            throw new RuntimeException("Error while fetching shift by ID", e);
        }
    }

    @Override
    public ShiftDTO updateShift(Long id, ShiftDTO shift) {
        try {
            Shift existing = shiftRepository.findById(id)
                    .orElseThrow(() -> new ShiftNotFoundException("Shift not found with ID: " + id));

            if (!existing.getEmployee().getEmployeeId().equals(shift.getEmployeeId())) {
                throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
            }

            existing.setShiftDate(shift.getShiftDate());
            existing.setShiftStartTime(shift.getShiftStartTime());
            existing.setShiftEndTime(shift.getShiftEndTime());

            return mapper.toDTo(shiftRepository.save(existing));
        } catch (ShiftNotFoundException | IllegalArgumentException e) {
            logger.warn("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating shift with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update shift", e);
        }
    }

    @Override
    public void deleteShift(Long id) {
        try {
            if (!shiftRepository.existsById(id)) {
                throw new ShiftNotFoundException("Cannot delete. Shift not found with ID: " + id);
            }
            shiftRepository.deleteById(id);
        } catch (ShiftNotFoundException e) {
            logger.warn("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting shift with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete shift", e);
        }
    }
}
