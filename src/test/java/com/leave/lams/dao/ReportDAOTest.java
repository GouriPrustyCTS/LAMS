package com.leave.lams.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave.lams.model.Employee;
import com.leave.lams.model.Report;
import com.leave.lams.repository.ReportRepository;

public class ReportDAOTest {

    @InjectMocks
    private ReportDAO reportDAO;

    @Mock
    private ReportRepository reportRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReport() {
        Report report = new Report();
        when(reportRepository.save(report)).thenReturn(report);

        Report result = reportDAO.createReport(report);
        assertEquals(report, result);
    }

    @Test
    public void testGetAllReports() {
        List<Report> reports = Arrays.asList(new Report(), new Report());
        when(reportRepository.findAll()).thenReturn(reports);

        List<Report> result = reportDAO.getAllReports();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetReportById() {
        Report report = new Report();
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        Optional<Report> result = reportDAO.getReportById(1L);
        assertTrue(result.isPresent());
        assertEquals(report, result.get());
    }

    @Test
    public void testUpdateReport() throws ParseException {
        String startDateString = "2023-01-01";
        String endDateString = "2023-01-01";
        String generatedDateString = "2023-01-01";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateString);
        Date endDate = dateFormat.parse(endDateString);
        Date generatedDate = dateFormat.parse(generatedDateString);

        // Convert java.util.Date to LocalDate
        LocalDate generatedLocalDate = generatedDate.toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDate();

        // Convert LocalDate to LocalDateTime (assuming you want the start of the day)
        LocalDateTime generatedLocalDateTime = generatedLocalDate.atStartOfDay();

        Report existingReport = new Report();
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        existingReport.setEmployee(employee);

        Report updatedReport = new Report();
        updatedReport.setEmployee(employee);
        updatedReport.setDateRangeStart(startDate);
        updatedReport.setDateRangeEnd(endDate);
        updatedReport.setGeneratedDate(generatedLocalDateTime); // Using LocalDateTime
        updatedReport.setTotalAttendance(20);
        updatedReport.setAbsenteesim(2);

        when(reportRepository.findById(1L)).thenReturn(Optional.of(existingReport));
        when(reportRepository.save(existingReport)).thenReturn(existingReport);

        Report result = reportDAO.updateReport(1L, updatedReport);
        assertEquals(existingReport, result);
        assertEquals(updatedReport.getDateRangeStart(), result.getDateRangeStart());
        assertEquals(updatedReport.getDateRangeEnd(), result.getDateRangeEnd());
        assertEquals(updatedReport.getGeneratedDate(), result.getGeneratedDate());
        assertEquals(updatedReport.getTotalAttendance(), result.getTotalAttendance());
        assertEquals(updatedReport.getAbsenteesim(), result.getAbsenteesim());
    }
    
    @Test
    public void testDeleteReport() {
        doNothing().when(reportRepository).deleteById(1L);

        reportDAO.deleteReport(1L);
        verify(reportRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void testUpdateReport_EmployeeMismatch() {
        Report existing = new Report();
        Employee emp1 = new Employee();
        emp1.setEmployeeId(1L);
        existing.setEmployee(emp1);

        Report updated = new Report();
        Employee emp2 = new Employee();
        emp2.setEmployeeId(2L); // mismatch
        updated.setEmployee(emp2);

        when(reportRepository.findById(1L)).thenReturn(Optional.of(existing));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reportDAO.updateReport(1L, updated);
        });

        assertEquals("Employee ID does not match the owner of this record.", exception.getMessage());
    }

}
