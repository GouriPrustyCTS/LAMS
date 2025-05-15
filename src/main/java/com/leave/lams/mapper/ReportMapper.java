package com.leave.lams.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leave.lams.dto.ReportDTO;
import com.leave.lams.model.Report;

@Component
public class ReportMapper {
	@Autowired
	private ModelMapper modelMapper;

	public Report toEntity(ReportDTO dto) {
		return modelMapper.map(dto, Report.class);
	}

	public ReportDTO toDTo(Report entity) {
		return modelMapper.map(entity, ReportDTO.class);
	}
}
