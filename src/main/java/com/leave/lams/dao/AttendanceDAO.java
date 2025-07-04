package com.leave.lams.dao;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.AttendanceDTO;
import com.leave.lams.exception.AttendanceNotFoundException;
import com.leave.lams.exception.InvalidInputException;
import com.leave.lams.mapper.AttendanceMapper;
import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
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
		Optional<Employee> employee = employeeRepository.findById(employeeId);

		if (employee.isPresent()) {
			LocalDate currentDate = LocalDate.now();
			List<AttendanceDTO> todaysDateAttendance = getAttendanceByDate(currentDate);

			List<AttendanceDTO> todaysDateAttendanceofEmployee = todaysDateAttendance.stream()
					.filter(a -> a.getEmployeeId() == employeeId).filter(a -> a.getClockOutTime() == null)
					.collect(Collectors.toList());

			Optional<AttendanceDTO> minClockTimeAttendance = todaysDateAttendanceofEmployee.stream()
					.min(Comparator.comparing(AttendanceDTO::getClockInTime));

			AttendanceDTO attendanceDto = new AttendanceDTO();
			if (minClockTimeAttendance.isPresent()) {
				attendanceDto = minClockTimeAttendance.get();
				throw new RuntimeException(
						"Employee with ID: " + employeeId + " already clocked in at " + attendanceDto.getClockInTime());
			} else {

				attendanceDto.setEmployeeId(employeeId);
				attendanceDto.setName(employee.get().getName());
				attendanceDto.setClockInTime(LocalDateTime.now());
				attendanceDto.setAttendanceDate(LocalDate.now());
				Attendance attendance = mapper.toEntity(attendanceDto);
				attendanceRepository.save(attendance);
			}

		} else {
			throw new RuntimeException("Employee not found with ID: " + employeeId);
		}

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
		attendanceDto.setWorkHours(
				(double) Duration.between(attendanceDto.getClockInTime(), attendanceDto.getClockOutTime()).toHours());

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

		return attendances.stream().map(mapper::toDTo).collect(Collectors.toList());
	}

	@Override
	public Double calculateWorkHours(Long attendanceId) {
		Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(
				() -> new AttendanceNotFoundException("Attendance record not found with ID: " + attendanceId));

		if (attendance.getClockOutTime() == null) {
			throw new AttendanceNotFoundException("Clock-out time not recorded for attendance ID: " + attendanceId);
		}

		return (double) Duration.between(attendance.getClockInTime(), attendance.getClockOutTime()).toHours();
	}

	@Override
	public AttendanceDTO updateAttendance(Long id, AttendanceDTO attendanceDto) {
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
