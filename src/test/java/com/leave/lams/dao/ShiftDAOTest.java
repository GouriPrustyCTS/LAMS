package com.leave.lams.dao;

import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.exception.ShiftNotFoundException;
import com.leave.lams.mapper.ShiftMapper;
import com.leave.lams.model.Employee;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftDAOTest {

    @InjectMocks
    private ShiftDAO shiftDAO;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private ShiftMapper shiftMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ShiftDTO createSampleShiftDTO() {
        ShiftDTO dto = new ShiftDTO();
        dto.setShiftDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setShiftStartTime(LocalTime.of(9, 0));
        dto.setShiftEndTime(LocalTime.of(17, 0));
        dto.setEmployeeId(1L);
        return dto;
    }

    private Shift createSampleShift() {
        Shift shift = new Shift();
        shift.setShiftDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        shift.setShiftStartTime(LocalTime.of(9, 0));
        shift.setShiftEndTime(LocalTime.of(17, 0));
        Employee emp = new Employee();
        emp.setEmployeeId(1L);
        shift.setEmployee(emp);
        return shift;
    }

    @Test
    void testCreateShift_Success() {
        ShiftDTO dto = createSampleShiftDTO();
        Shift shift = createSampleShift();

        when(shiftMapper.toEntity(dto)).thenReturn(shift);
        when(shiftRepository.save(shift)).thenReturn(shift);
        when(shiftMapper.toDTo(shift)).thenReturn(dto);

        ShiftDTO result = shiftDAO.createShift(dto);
        assertNotNull(result);
        verify(shiftRepository).save(shift);
    }

    @Test
    void testCreateShift_PastDateTime_ThrowsException() {
        ShiftDTO dto = createSampleShiftDTO();
        dto.setShiftDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> shiftDAO.createShift(dto));
        assertEquals("Shift date and time must not be less than the current date and time.", exception.getMessage());
    }

    @Test
    void testGetAllShifts() {
        List<Shift> shifts = List.of(createSampleShift());
        when(shiftRepository.findAll()).thenReturn(shifts);
        when(shiftMapper.toDTo(any())).thenReturn(createSampleShiftDTO());

        List<ShiftDTO> result = shiftDAO.getAllShifts();
        assertEquals(1, result.size());
    }

    @Test
    void testGetShiftById_Found() {
        Shift shift = createSampleShift();
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(shiftMapper.toDTo(shift)).thenReturn(createSampleShiftDTO());

        Optional<ShiftDTO> result = shiftDAO.getShiftById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testGetShiftById_NotFound() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ShiftNotFoundException.class, () -> shiftDAO.getShiftById(1L));
    }

    @Test
    void testUpdateShift_Success() {
        Shift existing = createSampleShift();
        ShiftDTO dto = createSampleShiftDTO();

        when(shiftRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(shiftRepository.save(any())).thenReturn(existing);
        when(shiftMapper.toDTo(existing)).thenReturn(dto);

        ShiftDTO result = shiftDAO.updateShift(1L, dto);
        assertNotNull(result);
    }

    @Test
    void testUpdateShift_EmployeeMismatch() {
        Shift existing = createSampleShift();
        ShiftDTO dto = createSampleShiftDTO();
        dto.setEmployeeId(999L); // Mismatched ID

        when(shiftRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> shiftDAO.updateShift(1L, dto));
    }

    @Test
    void testDeleteShift_Success() {
        when(shiftRepository.existsById(1L)).thenReturn(true);
        doNothing().when(shiftRepository).deleteById(1L);

        assertDoesNotThrow(() -> shiftDAO.deleteShift(1L));
    }

    @Test
    void testDeleteShift_NotFound() {
        when(shiftRepository.existsById(1L)).thenReturn(false);
        assertThrows(ShiftNotFoundException.class, () -> shiftDAO.deleteShift(1L));
    }
}
