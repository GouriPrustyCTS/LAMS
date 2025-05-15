package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.mapper.ShiftMapper;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.ShiftRepository;
import com.leave.lams.service.ShiftService;

@Service
public class ShiftDAO implements ShiftService {

	@Autowired
	private ShiftRepository shiftRepository;

	@Autowired
	private ShiftMapper mapper;

	public ShiftDTO createShift(ShiftDTO shiftDto) {
		Shift shift = mapper.toEntity(shiftDto);
		Shift savedShift = shiftRepository.save(shift);
		ShiftDTO dtoRes = mapper.toDTo(savedShift);
		return dtoRes;
	}

	public List<ShiftDTO> getAllShifts() {
		List<Shift> shifts = shiftRepository.findAll();
		return shifts.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	public Optional<ShiftDTO> getShiftById(Long shiftID) {
		Optional<Shift> shift = shiftRepository.findById(shiftID);
		    if (shift.isPresent()) {
		        return Optional.of(mapper.toDTo(shift.get()));
		    }
		    return Optional.empty();
	}

	@Override
	public ShiftDTO updateShift(Long id, ShiftDTO shift) {
		Optional<Shift> existing = shiftRepository.findById(id);
		if (existing.isPresent()) {
			Shift s = existing.get();

			if (!s.getEmployee().getEmployeeId().equals(shift.getEmployeeId())) {
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}

			s.setShiftDate(shift.getShiftDate());
			s.setShiftStartTime(shift.getShiftStartTime());
			s.setShiftEndTime(shift.getShiftEndTime());
			Shift savedShift = shiftRepository.save(s);
			ShiftDTO dtoRes = mapper.toDTo(savedShift);
			return dtoRes;
		}
		return null;
	}

	@Override
	public void deleteShift(Long id) {
		shiftRepository.deleteById(id);
	}
}
