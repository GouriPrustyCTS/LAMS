package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.EmployeeDTO;
import com.leave.lams.model.Employee;


@Component
public class EmployeeMapper {
	@Autowired
	private ModelMapper modelMapper;

	public Employee toEntity(EmployeeDTO dto) {
		return modelMapper.map(dto, Employee.class);
	}

	public EmployeeDTO toDTo(Employee entity) {
		return modelMapper.map(entity, EmployeeDTO.class);
	}
}
