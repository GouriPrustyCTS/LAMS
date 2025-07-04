package com.leave.lams.service;
import java.util.List;

import com.leave.lams.dto.EmployeeDTO;

public interface EmployeeService {


    public List<EmployeeDTO> getAllEmployees();

    public EmployeeDTO getEmployeeById(Long id) ;

    public EmployeeDTO addEmployee(EmployeeDTO employee) ;
    
    public EmployeeDTO updateEmployee(long id,EmployeeDTO employee);

    public void deleteEmployee(Long id) ;
}

