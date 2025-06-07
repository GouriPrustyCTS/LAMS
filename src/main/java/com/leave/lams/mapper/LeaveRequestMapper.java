package com.leave.lams.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
		LeaveRequestDTO dto = modelMapper.map(entity, LeaveRequestDTO.class);
		if (entity.getEmployee() != null) {
			dto.setName(entity.getEmployee().getName());
		}
		return dto;
	}
}
