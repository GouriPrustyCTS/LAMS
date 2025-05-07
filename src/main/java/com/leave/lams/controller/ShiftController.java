package com.leave.lams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.leave.lams.model.Report;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.ShiftRepository;
import com.leave.lams.service.ShiftService;

import java.util.List;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @PostMapping("/shifts")
    public Shift createShift(@RequestBody Shift shift) {
        return shiftService.createShift(shift);
    }

    @GetMapping("/")
    public List<Shift> getAllShifts() {
        return shiftService.getAllShifts();
    }

    @GetMapping("/{shiftID}")
    public ResponseEntity<Shift> getShiftById(@PathVariable Long shiftID) {
        return shiftService.getShiftById(shiftID)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/update/{id}")
    public Shift update(@PathVariable Long id, @RequestBody Shift shift) {
    	return shiftService.updateShift(id, shift);
    }
    
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
    	shiftService.deleteShift(id);
    }
}

