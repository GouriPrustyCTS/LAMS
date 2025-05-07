package com.leave.lams.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
