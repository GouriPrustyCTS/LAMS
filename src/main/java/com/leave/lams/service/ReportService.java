package com.leave.lams.service;

import com.leave.lams.dto.ReportDTO;
import com.leave.lams.model.Report;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.knowm.xchart.PieChart;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

    public ReportDTO createReport(ReportDTO report) ;

    public List<ReportDTO> getAllReports() ;

    public Optional<ReportDTO> getReportById(Long reportID) ;
    
    public ReportDTO updateReport(Long id, ReportDTO report);
     
    public void deleteReport(Long id);

	public PieChart generatePieChart();

	public byte[] generateTimeDifferenceBarChart(Long empId) throws IOException;

	public byte[] generateMonthWiseLeaveCountLineChart() throws IOException;

	public byte[] generateYearWiseLeaveCountLineChart() throws IOException;
}
