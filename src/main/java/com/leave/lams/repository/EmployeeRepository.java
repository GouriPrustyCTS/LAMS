package com.leave.lams.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String username);
	
	@Query("SELECT e.id FROM Employee e WHERE e.email = :email")
    long findEmployeeIdByEmail(@Param("email") String email);
}
