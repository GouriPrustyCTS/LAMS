package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.leave.lams.dto.ReportDTO;
import com.leave.lams.mapper.ReportMapper;
import com.leave.lams.model.Report;
import com.leave.lams.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReportDAOTest {

    @InjectMocks
    private ReportDAO reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportMapper reportMapper;

    private ReportDTO dto1, dto2, dto;
    private Report entity1, entity2, entity, existing;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dto1 = new ReportDTO();
        dto2 = new ReportDTO();
        dto = new ReportDTO();
        entity1 = new Report();
        entity2 = new Report();
        entity = new Report();
        existing = new Report();

        dto.setReportId(1L);
        dto.setTotalAttendance(20);
        dto.setAbsenteesim(2);
        dto.setGeneratedDate(LocalDateTime.now());
    }

    @Test
    public void testGetAllReports() {
        when(reportRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(reportMapper.toDTo(entity1)).thenReturn(dto1);
        when(reportMapper.toDTo(entity2)).thenReturn(dto2);

        List<ReportDTO> result = reportService.getAllReports();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetReportById() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(reportMapper.toDTo(entity)).thenReturn(dto);

        Optional<ReportDTO> result = reportService.getReportById(1L);
        assertEquals(dto, result);
    }

    @Test
    public void testCreateReport() {
        when(reportMapper.toEntity(dto)).thenReturn(entity);
        when(reportRepository.save(entity)).thenReturn(entity);
        when(reportMapper.toDTo(entity)).thenReturn(dto);

        ReportDTO result = reportService.createReport(dto);
        assertEquals(dto, result);
    }

    @Test
    public void testUpdateReport() {
        Long reportId = 1L;
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(existing));
        when(reportMapper.toEntity(dto)).thenReturn(existing);
        when(reportRepository.save(existing)).thenReturn(existing);
        when(reportMapper.toDTo(existing)).thenReturn(dto);

        ReportDTO result = reportService.updateReport(reportId, dto);
        assertEquals(dto, result);
    }

    @Test
    public void testDeleteReport() {
        Long reportId = 1L;
        when(reportRepository.existsById(reportId)).thenReturn(true);
        reportService.deleteReport(reportId);
        verify(reportRepository, times(1)).deleteById(reportId);
    }

    @Test
    public void testUpdateReport_EmployeeMismatch() {
        entity.setReportId(1L);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(entity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reportService.updateReport(1L, dto);
        });

        assertEquals("Employee ID does not match the owner of this record.", exception.getMessage());
    }
}
