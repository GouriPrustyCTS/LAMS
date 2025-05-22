package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leave.lams.dto.EmployeeDTO;
import com.leave.lams.mapper.EmployeeMapper;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeDAOTest {

    @InjectMocks
    private EmployeeDAO employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    private EmployeeDTO dto1, dto2, dto;
    private Employee entity1, entity2, entity, existing;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dto1 = new EmployeeDTO();
        dto2 = new EmployeeDTO();
        dto = new EmployeeDTO();
        entity1 = new Employee();
        entity2 = new Employee();
        entity = new Employee();
        existing = new Employee();

        dto.setEmployeeId(1L);
        dto.setName("John Doe");
        dto.setEmail("john.doe@example.com");
    }

    @Test
    public void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(employeeMapper.toDTo(entity1)).thenReturn(dto1);
        when(employeeMapper.toDTo(entity2)).thenReturn(dto2);

        List<EmployeeDTO> result = employeeService.getAllEmployees();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeMapper.toDTo(entity)).thenReturn(dto);

        EmployeeDTO result = employeeService.getEmployeeById(1L);
        assertEquals(dto, result);
    }

    @Test
    public void testAddEmployee() {
        when(employeeMapper.toEntity(dto)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(entity);
        when(employeeMapper.toDTo(entity)).thenReturn(dto);

        EmployeeDTO result = employeeService.addEmployee(dto);
        assertEquals(dto, result);
    }

    @Test
    public void testUpdateEmployee() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existing));
        when(employeeMapper.toEntity(dto)).thenReturn(existing);
        when(employeeRepository.save(existing)).thenReturn(existing);
        when(employeeMapper.toDTo(existing)).thenReturn(dto);

        EmployeeDTO result = employeeService.updateEmployee(employeeId, dto);
        assertEquals(dto, result);
    }

    @Test
    public void testDeleteEmployee() {
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        employeeService.deleteEmployee(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
