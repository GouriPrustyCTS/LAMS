package com.leave.lams.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Employee;
import com.leave.lams.repository.EmployeeRepository;

@Service
public class EmployeeDetailsService implements UserDetailsService {	// implement the creation of UserDetails by overriding loadbyUsername

    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Admin login
        if ("root@gmail.com".equals(username)) {
            return User.withUsername("root@gmail.com")
                    .password(new BCryptPasswordEncoder().encode("root"))
                    .roles("ADMIN")
                    .build();
        }

        // Employee login
        Optional<Employee> emp = employeeRepo.findByEmail(username);
        if(emp.isPresent()) {
        	return User.withUsername(emp.get().getEmail())
                    .password(emp.get().getPassword())
                    .roles("EMPLOYEE")
                    .build();
        }
        
        return null;
        
    }
}

