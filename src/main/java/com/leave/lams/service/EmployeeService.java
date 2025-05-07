package com.leave.lams.service;
import com.leave.lams.model.Employee;
import java.util.List;

public interface EmployeeService {


    public List<Employee> getAllEmployees();

    public Employee getEmployeeById(Long id) ;

    public Employee addEmployee(Employee employee) ;
    
    public Employee updatEmployee(long id,Employee employee);

    public void deleteEmployee(Long id) ;
}

