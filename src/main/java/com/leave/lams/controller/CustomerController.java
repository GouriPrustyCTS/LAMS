package com.leave.lams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.dao.CustomerServiceImpl;
import com.leave.lams.model.Customer;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerServiceImpl service;
	
	@PostMapping("/addCustomer")
	public Customer saveCustomer(@RequestBody Customer c) {
		return service.addCustomer(c);
	}
}