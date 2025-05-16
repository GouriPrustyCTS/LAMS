package com.leave.lams.dao;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.AttendanceDTO;
import com.leave.lams.exception.AttendanceNotFoundException;
import com.leave.lams.exception.EmployeeNotFoundException;
import com.leave.lams.exception.InvalidInputException;
import com.leave.lams.mapper.AttendanceMapper;
import com.leave.lams.model.Attendance;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.AttendanceService;

@Service
public class AttendanceDAO implements AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AttendanceMapper mapper;

	public List<AttendanceDTO> getAllAttendances() {
		List<Attendance> attendances = attendanceRepository.findAll();
		if (attendances.isEmpty()) {
			throw new AttendanceNotFoundException("No attendance records found.");
		}
		return attendances.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	public AttendanceDTO getAttendanceById(Long id) {
		return attendanceRepository.findById(id).map(mapper::toDTo)
				.orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with ID: " + id));
	}

	public AttendanceDTO addAttendance(AttendanceDTO attendanceDto) {
		if (attendanceDto.getEmployeeId() == null) {
			throw new InvalidInputException("Employee must be associated with an attendance record.");
		}
		Attendance attendance = mapper.toEntity(attendanceDto);
		return mapper.toDTo(attendanceRepository.save(attendance));
	}



	public void deleteAttendance(Long id) {
		if (!attendanceRepository.existsById(id)) {
			throw new AttendanceNotFoundException("Attendance record not found with ID: " + id);
		}
		attendanceRepository.deleteById(id);
	}

	@Override
	public void clockIn(Long employeeId) {
		employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));
 
        AttendanceDTO attendanceDto = new AttendanceDTO();
        attendanceRepository.save(mapper.toEntity(attendanceDto));

	}

	@Override
	public void clockOut(Long employeeId) {
		List<Attendance> attendances = attendanceRepository.findLatestByEmployeeId(employeeId);
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("Clock-in record not found for employee ID: " + employeeId);
        }
 
        Attendance attendance = attendances.get(0);
        if (attendance.getClockOutTime() != null) {
            throw new InvalidInputException("Employee has already clocked out.");
        }
 
        AttendanceDTO attendanceDto = mapper.toDTo(attendance);
        attendanceDto.setClockOutTime(LocalDateTime.now());
        attendanceDto.setWorkHours((double) Duration.between(attendanceDto.getClockInTime(), attendanceDto.getClockOutTime()).toHours());
 
        attendanceRepository.save(mapper.toEntity(attendanceDto));

	}

	@Override
	public List<AttendanceDTO> getAttendanceByEmployee(Long employeeId) {
		  List<Attendance> attendances = attendanceRepository.findByEmployee_EmployeeId(employeeId);
	        if (attendances.isEmpty()) {
	            throw new AttendanceNotFoundException("No attendance records found for employee ID: " + employeeId);
	        }
	        return attendances.stream().map(mapper::toDTo).collect(Collectors.toList());
	    }
	 
	@Override
    public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {
        List<Attendance> attendances = attendanceRepository.findByAttendanceDate(date);
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance records found for date: " + date);
        }
        return attendances.stream().map(mapper::toDTo).collect(Collectors.toList());
	}
	
	@Override
	public Double calculateWorkHours(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with ID: " + attendanceId));
 
        if (attendance.getClockOutTime() == null) {
            throw new AttendanceNotFoundException("Clock-out time not recorded for attendance ID: " + attendanceId);
        }
 
        return (double) Duration.between(attendance.getClockInTime(), attendance.getClockOutTime()).toHours();
    }

	@Override
	public AttendanceDTO updatAttendance(Long id, AttendanceDTO attendanceDto) {
		Attendance existingAttendance = attendanceRepository.findById(id)
				.orElseThrow(() -> new AttendanceNotFoundException("Attendance record not found with ID: " + id));

		if (attendanceDto.getEmployeeId() == null
				|| !existingAttendance.getEmployee().getEmployeeId().equals(attendanceDto.getEmployeeId())) {
			throw new InvalidInputException("Employee ID does not match the owner of this record.");
		}

		Attendance updatedAttendance = mapper.toEntity(attendanceDto);
		updatedAttendance.setAttendanceId(id);
		return mapper.toDTo(attendanceRepository.save(updatedAttendance));

	}


}
