package com.leave.lams.controller;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.leave.lams.model.Employee;
import com.leave.lams.repository.EmployeeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeDAO employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

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
		// FIX: ASSUMING employeeService.getEmployeeById(id) ALREADY RETURNS Optional<EmployeeDTO>
		Optional<EmployeeDTO> employeeDtoOptional = Optional.of(employeeService.getEmployeeById(id));

		ResponseEntity<EmployeeDTO> response = employeeDtoOptional
				.map(employeeDto -> {
					logger.info("Response sent: GET /employee/{} - Employee found", id);
					return ResponseEntity.ok(employeeDto);
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
	public EmployeeDTO update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employee) {
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

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Request received: GET /employee/details for user: {}", userDetails.getUsername());
        Map<String, Object> details = new HashMap<>();
        details.put("email", userDetails.getUsername()); // This is the email (username)

        // Fetch additional details from your Employee entity/DB based on the username (email)
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(userDetails.getUsername());
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            // Using employee.getName() for full name as per your Employee model
            details.put("name", employee.getName());
            // Using employee.getDesignation() which returns jobTitle as per your Employee model
            details.put("title", employee.getDesignation());
        } else {
            // Fallback if employee details are not found (shouldn't happen if user is authenticated and data is consistent)
            details.put("name", "Unknown User");
            details.put("title", "N/A");
            logger.warn("Employee details not found in database for authenticated user: {}", userDetails.getUsername());
        }

        // Add roles from UserDetails
        details.put("roles", userDetails.getAuthorities().stream()
                                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "")) // Remove "ROLE_" prefix
                                .collect(Collectors.toList()));

        logger.info("Response sent: GET /employee/details - Details retrieved for {}", userDetails.getUsername());
        return ResponseEntity.ok(details);
    }
}