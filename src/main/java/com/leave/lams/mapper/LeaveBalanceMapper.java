package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.LeaveBalanceDTO;
import com.leave.lams.model.LeaveBalance;


@Component
public class LeaveBalanceMapper {
	@Autowired
	private ModelMapper modelMapper;

	public LeaveBalance toEntity(LeaveBalanceDTO dto) {
		return modelMapper.map(dto, LeaveBalance.class);
	}

	public LeaveBalanceDTO toDTo(LeaveBalance entity) {
		LeaveBalanceDTO dto = modelMapper.map(entity, LeaveBalanceDTO.class);
		if (entity.getEmployee() != null) {
			dto.setName(entity.getEmployee().getName());
		}
		return dto;
	}
}
