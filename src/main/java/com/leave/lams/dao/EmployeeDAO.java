package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployees() {
		logger.info("Getting all employees");
		List<Employee> employees = employeeRepository.findAll();
		logger.info("Retrieved {} employees", employees.size());
		return employees;
	}

	public Optional<Employee> getEmployeeById(Long id) {
		logger.info("Getting employee by id: {}", id);
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			logger.info("Employee found with id: {}", id);
		} else {
			logger.warn("Employee not found with id: {}", id);
		}
		return employee;
	}

	@Transactional
	public Employee addEmployee(Employee employee) {
		logger.info("Adding employee: {}", employee);

		if (employee.getAttendances() != null) {
			for (Attendance attendance : employee.getAttendances()) {
				attendance.setEmployee(employee);
				logger.debug("Setting employee for attendance: {}", attendance);
			}
		}

		// Set employee reference in leaveRequests
		if (employee.getLeaveRequests() != null) {
			for (LeaveRequest leaveRequest : employee.getLeaveRequests()) {
				leaveRequest.setEmployee(employee);
				logger.debug("Setting employee for leaveRequest: {}", leaveRequest);
			}
		}

		// Set employee reference in leaveBalances
		if (employee.getLeaveBalances() != null) {
			for (LeaveBalance leaveBalance : employee.getLeaveBalances()) {
				leaveBalance.setEmployee(employee);
				logger.debug("Setting employee for leaveBalance: {}", leaveBalance);
			}
		}

		// Set employee reference in shifts
		if (employee.getShifts() != null) {
			for (Shift shift : employee.getShifts()) {
				shift.setEmployee(employee);
				logger.debug("Setting employee for shift: {}", shift);
			}
		}

		// Set employee reference in reports
		if (employee.getReports() != null) {
			for (Report report : employee.getReports()) {
				report.setEmployee(employee);
				logger.debug("Setting employee for report: {}", report);
			}
		}
		Employee savedEmployee = employeeRepository.save(employee);
		logger.info("Employee added with id: {}", savedEmployee.getEmployeeId());
		return savedEmployee;
	}

	@Override
	public Employee updateEmployee(long id, Employee employee) {
		logger.info("Updating employee with id: {}, data: {}", id, employee);
		Optional<Employee> existingOptional = employeeRepository.findById(id);
		if (existingOptional.isPresent()) {
			Employee emp = existingOptional.get();
			
			if(!emp.getEmployeeId().equals(employee.getEmployeeId())) {
				logger.error("Employee ID does not match the owner of this record.");
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}
			
			emp.setName(employee.getName());
			emp.setEmail(employee.getEmail());
			Employee updatedEmployee = employeeRepository.save(emp);
			logger.info("Employee updated with id: {}", updatedEmployee.getEmployeeId());
			return updatedEmployee;
		}
		logger.warn("Employee with id: {} not found.", id);
		return null;
	}

	public void deleteEmployee(Long id) {
		logger.info("Deleting employee with id: {}", id);
		employeeRepository.deleteById(id);
		logger.info("Employee deleted with id: {}", id);
	}

}
