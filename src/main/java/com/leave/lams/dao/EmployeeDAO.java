package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.EmployeeDTO;
import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.mapper.EmployeeMapper;
import com.leave.lams.model.Attendance;
import com.leave.lams.model.Employee;
import com.leave.lams.model.LeaveBalance;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.model.Report;
import com.leave.lams.model.Shift;
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
		return employees.stream().map(s -> mapper.toDTo(s)).collect(Collectors.toList());
	}

	public Optional<EmployeeDTO> getEmployeeById(Long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			 return Optional.of(mapper.toDTo(employee.get()));
		} else {
			return Optional.empty();
		}
	}

	@Transactional
	public EmployeeDTO addEmployee(EmployeeDTO employee) {
		Employee emp = mapper.toEntity(employee);
		Employee savedEmployee = employeeRepository.save(emp);
		EmployeeDTO dtoRes = mapper.toDTo(savedEmployee);
		return dtoRes;
	}

	@Override
	public EmployeeDTO updateEmployee(long id, EmployeeDTO employee) {
		Optional<Employee> existingOptional = employeeRepository.findById(id);
		if (existingOptional.isPresent()) {
			Employee emp = existingOptional.get();
			
			if(!emp.getEmployeeId().equals(employee.getEmployeeId())) {
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}
			
			emp.setName(employee.getName());
			emp.setEmail(employee.getEmail());
			Employee updatedEmployee = employeeRepository.save(emp);
			return mapper.toDTo(updatedEmployee);
		}
		return null;
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

}
