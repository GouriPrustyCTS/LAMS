package com.leave.lams.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.leave.lams.dto.ShiftDTO;


@Service
public interface ShiftService {


    public ShiftDTO createShift(ShiftDTO shift) ;

    public List<ShiftDTO> getAllShifts() ;

    public Optional<ShiftDTO> getShiftById(Long shiftID) ;
    
    public ShiftDTO updateShift(Long id, ShiftDTO shift);
    
    public void deleteShift(Long id);
}
