package com.leave.lams.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.EmployeeDTO;
import com.leave.lams.exception.EmployeeNotFoundException;
import com.leave.lams.mapper.EmployeeMapper;
import com.leave.lams.model.Employee;
import com.leave.lams.repository.EmployeeRepository;
import com.leave.lams.service.EmployeeService;

import jakarta.transaction.Transactional;

@Service
public class EmployeeDAO implements EmployeeService {


	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeMapper mapper;

	public List<EmployeeDTO> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees found.");
        }
        List<EmployeeDTO> employeeDtos = employees.stream().map(mapper::toDTo).collect(Collectors.toList());
        return employeeDtos;
	}

	public EmployeeDTO getEmployeeById(Long id) {
		  Employee employee = employeeRepository.findById(id)
	                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
	        return mapper.toDTo(employee);
	}

	@Transactional
	public EmployeeDTO addEmployee(EmployeeDTO employeeDto) {
		Employee employee = mapper.toEntity(employeeDto);
		employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.toDTo(savedEmployee);
	}

	@Override
	public EmployeeDTO updateEmployee(long id, EmployeeDTO employeeDto) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
 
        Employee updatedEmployee = mapper.toEntity(employeeDto);
        updatedEmployee.setEmployeeId(id); // Ensure correct ID is retained
        updatedEmployee = employeeRepository.save(updatedEmployee);
 
        return mapper.toDTo(updatedEmployee);
	}

	public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
	}

}
