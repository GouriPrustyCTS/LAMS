package com.leave.lams.service;

import java.util.List;

import com.leave.lams.model.Customer;

public interface CustomerService {
	Customer addCustomer(Customer customer);
	List<Customer> getAllCustomer();
	int updateCustomer(long customerId, Customer cust);
}

