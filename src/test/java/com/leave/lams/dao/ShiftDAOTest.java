package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.ShiftRepository;

public class ShiftDAOTest {

	@InjectMocks
	private ShiftDAO shiftDAO;

	@Mock
	private ShiftRepository shiftRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateShift() {
		Shift shift = new Shift();
		when(shiftRepository.save(shift)).thenReturn(shift);

		Shift result = shiftDAO.createShift(shift);
		assertEquals(shift, result);
	}

	@Test
	public void testGetAllShifts() {
		List<Shift> shifts = Arrays.asList(new Shift(), new Shift());
		when(shiftRepository.findAll()).thenReturn(shifts);

		List<Shift> result = shiftDAO.getAllShifts();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetShiftById() {
		Shift shift = new Shift();
		when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));

		Optional<Shift> result = shiftDAO.getShiftById(1L);
		assertTrue(result.isPresent());
		assertEquals(shift, result.get());
	}

	@Test
    public void testUpdateShift() {

        Shift existingShift = new Shift();
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        existingShift.setEmployee(employee);

        Shift updatedShift = new Shift();
        updatedShift.setEmployee(employee);

        // Convert String to LocalDate
        LocalDate shiftLocalDate = LocalDate.parse("2023-01-01");

        // Convert LocalDate to java.util.Date (setting time to the beginning of the day)
        Date shiftDate = Date.from(shiftLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        updatedShift.setShiftDate(shiftDate);

        // Convert String to LocalTime
        LocalTime shiftStartTime = LocalTime.parse("09:00");
        updatedShift.setShiftStartTime(shiftStartTime);

        // Convert String to LocalTime
        LocalTime shiftEndTime = LocalTime.parse("17:00");
        updatedShift.setShiftEndTime(shiftEndTime);

        when(shiftRepository.findById(1L)).thenReturn(Optional.of(existingShift));
        when(shiftRepository.save(existingShift)).thenReturn(existingShift);

        Shift result = shiftDAO.updateShift(1L, updatedShift);
        assertEquals(existingShift, result);
        assertEquals(updatedShift.getShiftDate(), result.getShiftDate());
        assertEquals(updatedShift.getShiftStartTime(), result.getShiftStartTime());
        assertEquals(updatedShift.getShiftEndTime(), result.getShiftEndTime());
    }
	@Test
	public void testDeleteShift() {
		doNothing().when(shiftRepository).deleteById(1L);

		shiftDAO.deleteShift(1L);
		verify(shiftRepository, times(1)).deleteById(1L);
	}
	
	@Test
	public void testUpdateShift_EmployeeMismatch() {
	    Shift existingShift = new Shift();
	    Employee emp1 = new Employee();
	    emp1.setEmployeeId(1L);
	    existingShift.setEmployee(emp1);

	    Shift updatedShift = new Shift();
	    Employee emp2 = new Employee();
	    emp2.setEmployeeId(2L); // mismatched employee ID
	    updatedShift.setEmployee(emp2);

	    when(shiftRepository.findById(1L)).thenReturn(Optional.of(existingShift));

	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	        shiftDAO.updateShift(1L, updatedShift);
	    });

	    assertEquals("Employee ID does not match the owner of this record.", exception.getMessage());
	}

}
