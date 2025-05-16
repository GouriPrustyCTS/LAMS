package com.leave.lams.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dao.EmployeeDAO;
import com.leave.lams.dto.EmployeeDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeDAO employeeService;

	@GetMapping("/")
	public List<EmployeeDTO> getAllEmployees() {
		logger.info("Request received: GET /employee/");
		List<EmployeeDTO> employees = employeeService.getAllEmployees();
		logger.info("Response sent: GET /employee/ - Retrieved {} employees", employees.size());
		return employees;
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
	    logger.info("Request received: GET /employee/{}", id);
	    Optional<EmployeeDTO> employeeDtoOptional = Optional.of(employeeService.getEmployeeById(id)) ; // Get the Optional
	    ResponseEntity<EmployeeDTO> response = employeeDtoOptional
	        .map(employeeDto -> { // Use map to transform the EmployeeDTO *within* the Optional
	            logger.info("Response sent: GET /employee/{} - Employee found", id);
	            return ResponseEntity.ok(employeeDto); // *Return* a ResponseEntity.ok()
	        })
	        .orElseGet(() -> {
	            logger.warn("Response sent: GET /employee/{} - Employee not found", id);
	            return ResponseEntity.notFound().build();
	        });
	    return response;
	}

	@PostMapping("/add")
	public EmployeeDTO createEmployee(@Valid @RequestBody EmployeeDTO employee) {
		logger.info("Request received: POST /employee/add - Request Body: {}", employee);
		EmployeeDTO createdEmployee = employeeService.addEmployee(employee);
		logger.info("Response sent: POST /employee/add - Employee created with ID: {}", createdEmployee.getEmployeeId());
		return createdEmployee;
	}

	@PutMapping("/{id}")
	public EmployeeDTO update(@PathVariable Long id,@Valid @RequestBody EmployeeDTO employee) {
		logger.info("Request received: PUT /employee/{} - Request Body: {}", id, employee);
		EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employee);
		logger.info("Response sent: PUT /employee/{} - Employee updated", id);
		return updatedEmployee;
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		logger.info("Request received: DELETE /employee/{}", id);
		employeeService.deleteEmployee(id);
		logger.info("Response sent: DELETE /employee/{} - Employee deleted", id);
	}

}
