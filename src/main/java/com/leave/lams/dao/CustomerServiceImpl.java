package com.leave.lams.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Customer;
import com.leave.lams.repository.CustomerRepository;
import com.leave.lams.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerRepository repository;

	@Override
	public Customer addCustomer(Customer customer) {
		return repository.save(customer);
	}

	@Override
	public List<Customer> getAllCustomer() {
		return repository.findAll();
	}

	@Override
	public int updateCustomer(long customerId, Customer cust) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

