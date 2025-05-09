package com.leave.lams.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.model.Report;
import com.leave.lams.service.ReportService;

@RestController
@RequestMapping("/attendanceReports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/add")
    public Report createReport(@RequestBody Report report) {
        return reportService.createReport(report);
    }

    @GetMapping("/")
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{reportID}")
    public ResponseEntity<Report> getReportById(@PathVariable Long reportID) {
        return reportService.getReportById(reportID)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public Report update(@PathVariable Long id, @RequestBody Report report) {
    	return reportService.updateReport(id, report);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	reportService.deleteReport(id);
    }
}

