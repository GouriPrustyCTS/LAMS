package com.leave.lams.service;

import com.leave.lams.model.Report;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface ReportService {

    public Report createReport(Report report) ;

    public List<Report> getAllReports() ;

    public Optional<Report> getReportById(Long reportID) ;
    
    public Report updateReport(Long id, Report report);
     
    public void deleteReport(Long id);
}
