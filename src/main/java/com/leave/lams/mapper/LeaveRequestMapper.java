package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.LeaveRequestDTO;
import com.leave.lams.model.LeaveRequest;

@Component
public class LeaveRequestMapper {
	@Autowired
	private ModelMapper modelMapper;

	public LeaveRequest toEntity(LeaveRequestDTO dto) {
		return modelMapper.map(dto, LeaveRequest.class);
	}

	public LeaveRequestDTO toDTo(LeaveRequest entity) {
		return modelMapper.map(entity, LeaveRequestDTO.class);
	}
}
