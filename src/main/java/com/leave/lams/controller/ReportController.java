package com.leave.lams.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.leave.lams.model.Report;
import com.leave.lams.repository.ReportRepository;
import com.leave.lams.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/attendanceReports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/reports")
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
    
    @PutMapping("/update/{id}")
    public Report update(@PathVariable Long id, @RequestBody Report report) {
    	return reportService.updateReport(id, report);
    }
    
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
    	reportService.deleteReport(id);
    }
}

