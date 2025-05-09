package com.leave.lams.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.model.Shift;
import com.leave.lams.service.ShiftService;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @PostMapping("/add")
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
    
    @PutMapping("/{id}")
    public Shift update(@PathVariable Long id, @RequestBody Shift shift) {
    	return shiftService.updateShift(id, shift);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	shiftService.deleteShift(id);
    }
}

