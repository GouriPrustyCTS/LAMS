package com.leave.lams.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.leave.lams.model.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
