package com.leave.lams.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Shift;
import com.leave.lams.repository.ShiftRepository;
import com.leave.lams.service.ShiftService;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftDAO implements ShiftService{

    @Autowired
    private ShiftRepository shiftRepository;

    public Shift createShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long shiftID) {
        return shiftRepository.findById(shiftID);
    }

	@Override
	public Shift updateShift(Long id, Shift shift) {
		Optional<Shift> existing = shiftRepository.findById(id);
		if(existing.isPresent()) {
			Shift s = existing.get();
			s.setShiftDate(shift.getShiftDate());
			s.setShiftStartTime(shift.getShiftStartTime());
			s.setShiftEndTime(shift.getShiftEndTime());
			return shiftRepository.save(s);
		}
		return null;
	}

	@Override
	public void deleteShift(Long id) {
		shiftRepository.deleteById(id);
	}
}

