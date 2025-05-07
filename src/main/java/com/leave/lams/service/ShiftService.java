package com.leave.lams.service;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Shift;

import java.util.List;
import java.util.Optional;


@Service
public interface ShiftService {


    public Shift createShift(Shift shift) ;

    public List<Shift> getAllShifts() ;

    public Optional<Shift> getShiftById(Long shiftID) ;
    
    public Shift updateShift(Long id, Shift shift);
    
    public void deleteShift(Long id);
}
