package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.ShiftSwapRequestDTO;
import com.leave.lams.model.ShiftSwapRequest;

@Component
public class ShiftSwapRequestMapper {
	@Autowired
	private ModelMapper modelMapper;

	public ShiftSwapRequest toEntity(ShiftSwapRequestDTO dto) {
		return modelMapper.map(dto, ShiftSwapRequest.class);
	}

	public ShiftSwapRequestDTO toDTo(ShiftSwapRequest entity) {
		return modelMapper.map(entity, ShiftSwapRequestDTO.class);
	}
}
