package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.ShiftDTO;
import com.leave.lams.model.Shift;

@Component
public class ShiftMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public Shift toEntity(ShiftDTO dto)
	{
		return modelMapper.map(dto, Shift.class);
	}
	
	public ShiftDTO toDTo(Shift entity)
	{
		ShiftDTO dto = modelMapper.map(entity, ShiftDTO.class);
		if (entity.getEmployee() != null) {
			dto.setName(entity.getEmployee().getName());
		}
		return dto;

	}

}
