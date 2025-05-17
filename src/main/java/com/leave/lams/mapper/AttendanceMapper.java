package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.AttendanceDTO;
import com.leave.lams.model.Attendance;

@Component
public class AttendanceMapper {
	@Autowired
	private ModelMapper modelMapper;

	public Attendance toEntity(AttendanceDTO dto) {
		return modelMapper.map(dto, Attendance.class);
	}

	public AttendanceDTO toDTo(Attendance entity) {
		AttendanceDTO dto = modelMapper.map(entity, AttendanceDTO.class);
		if (entity.getEmployee() != null) {
			dto.setName(entity.getEmployee().getName());
		}
		return dto;
	}

}
