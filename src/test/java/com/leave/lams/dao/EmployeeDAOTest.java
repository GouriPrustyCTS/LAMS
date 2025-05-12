package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.model.Report;
import com.leave.lams.model.Shift;
import com.leave.lams.repository.EmployeeRepository;

public class EmployeeDAOTest {

    @InjectMocks
    private EmployeeDAO employeeDAO;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeDAO.getAllEmployees();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeDAO.getEmployeeById(1L);
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

    @Test
    public void testAddEmployee() {
        Employee employee = new Employee();
        employee.setAttendances(Arrays.asList(new Attendance()));
        employee.setLeaveRequests(Arrays.asList(new LeaveRequest()));
        employee.setLeaveBalances(Arrays.asList(new LeaveBalance()));
        employee.setShifts(Arrays.asList(new Shift()));
        employee.setReports(Arrays.asList(new Report()));

        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeDAO.addEmployee(employee);
        assertEquals(employee, result);
        assertEquals(employee, employee.getAttendances().get(0).getEmployee());
        assertEquals(employee, employee.getLeaveRequests().get(0).getEmployee());
        assertEquals(employee, employee.getLeaveBalances().get(0).getEmployee());
        assertEquals(employee, employee.getShifts().get(0).getEmployee());
        assertEquals(employee, employee.getReports().get(0).getEmployee());
    }

    @Test
    public void testUpdateEmployee() {
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1L);

        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId(1L);
        newEmployee.setName("John Doe");
        newEmployee.setEmail("john.doe@example.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        Employee result = employeeDAO.updateEmployee(1L, newEmployee);
        assertEquals(existingEmployee, result);
        assertEquals(newEmployee.getName(), result.getName());
        assertEquals(newEmployee.getEmail(), result.getEmail());
    }

    @Test
    public void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeDAO.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void testUpdateEmployee_MismatchedId() {
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1L);

        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId(2L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));

        assertThrows(IllegalArgumentException.class, () -> {
            employeeDAO.updateEmployee(1L, newEmployee);
        });
    }

}

