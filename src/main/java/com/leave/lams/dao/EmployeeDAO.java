package com.leave.lams.dao;

import java.util.List;
import java.util.Optional;

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
	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	@Transactional
	public Employee addEmployee(Employee employee) {
		if (employee.getAttendances() != null) {
			for (Attendance attendance : employee.getAttendances()) {
				attendance.setEmployee(employee);
			}
		}

		// Set employee reference in leaveRequests
		if (employee.getLeaveRequests() != null) {
			for (LeaveRequest leaveRequest : employee.getLeaveRequests()) {
				leaveRequest.setEmployee(employee);
			}
		}

		// Set employee reference in leaveBalances
		if (employee.getLeaveBalances() != null) {
			for (LeaveBalance leaveBalance : employee.getLeaveBalances()) {
				leaveBalance.setEmployee(employee);
			}
		}

		// Set employee reference in shifts
		if (employee.getShifts() != null) {
			for (Shift shift : employee.getShifts()) {
				shift.setEmployee(employee);
			}
		}

		// Set employee reference in reports
		if (employee.getReports() != null) {
			for (Report report : employee.getReports()) {
				report.setEmployee(employee);
			}
		}
		return employeeRepository.save(employee);
	}

	@Override
	public Employee updatEmployee(long id, Employee employee) {
		Optional<Employee> existingOptional = employeeRepository.findById(id);
		if (existingOptional.isPresent()) {
			Employee emp = existingOptional.get();
			emp.setName(employee.getName());
			emp.setEmail(employee.getEmail());
			return employeeRepository.save(emp);
		}
		return null;
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

}