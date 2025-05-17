package com.leave.lams.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.leave.lams.model.Employee;
import com.leave.lams.model.Report;

public class ReportDTOTest {

    @Test
    public void testReportDTOCreation() {
        // Arrange
        Long reportId = 1L;
        Long employeeId = 101L;
        Date dateRangeStart = new Date();
        Date dateRangeEnd = new Date();
        LocalDateTime generatedDate = LocalDateTime.now();
        int totalAttendance = 20;
        int absenteeism = 2;

        // Act
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId(reportId);
        reportDTO.setEmployeeId(employeeId);
        reportDTO.setDateRangeStart(dateRangeStart);
        reportDTO.setDateRangeEnd(dateRangeEnd);
        reportDTO.setGeneratedDate(generatedDate);
        reportDTO.setTotalAttendance(totalAttendance);
        reportDTO.setAbsenteesim(absenteeism);

        // Assert
        assertEquals(reportId, reportDTO.getReportId());
        assertEquals(employeeId, reportDTO.getEmployeeId());
        assertEquals(dateRangeStart, reportDTO.getDateRangeStart());
        assertEquals(dateRangeEnd, reportDTO.getDateRangeEnd());
        assertEquals(generatedDate, reportDTO.getGeneratedDate());
        assertEquals(totalAttendance, reportDTO.getTotalAttendance());
        assertEquals(absenteeism, reportDTO.getAbsenteesim());
    }

    @Test
    public void testReportDTOFromModel() {
        // Arrange
        Employee employee = new Employee();
        employee.setEmployeeId(101L);

        Report report = new Report();
        report.setReportId(1L);
        report.setEmployee(employee);
        report.setDateRangeStart(new Date());
        report.setDateRangeEnd(new Date());
        report.setGeneratedDate(LocalDateTime.now());
        report.setTotalAttendance(20);
        report.setAbsenteesim(2);

        // Act
        ReportDTO reportDTO = new ReportDTO();

        // Assert
        assertNotNull(reportDTO);
        assertEquals(report.getReportId(), reportDTO.getReportId());
        assertEquals(report.getEmployee().getEmployeeId(), reportDTO.getEmployeeId());
        assertEquals(report.getDateRangeStart(), reportDTO.getDateRangeStart());
        assertEquals(report.getDateRangeEnd(), reportDTO.getDateRangeEnd());
        assertEquals(report.getGeneratedDate(), reportDTO.getGeneratedDate());
        assertEquals(report.getTotalAttendance(), reportDTO.getTotalAttendance());
        assertEquals(report.getAbsenteesim(), reportDTO.getAbsenteesim());
    }
}