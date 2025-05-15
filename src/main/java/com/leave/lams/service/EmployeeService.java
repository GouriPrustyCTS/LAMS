package com.leave.lams.service;
import java.util.List;
import java.util.Optional;

import com.leave.lams.dto.EmployeeDTO;

public interface EmployeeService {


    public List<EmployeeDTO> getAllEmployees();

    public Optional<EmployeeDTO> getEmployeeById(Long id) ;

    public EmployeeDTO addEmployee(EmployeeDTO employee) ;
    
    public EmployeeDTO updateEmployee(long id,EmployeeDTO employee);

    public void deleteEmployee(Long id) ;
}

