package com.leave.lams.controller;

import java.io.IOException;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leave.lams.model.Report;
import com.leave.lams.service.ReportService;

@RestController
@RequestMapping("/attendanceReports")
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	@PostMapping("/add")
	public Report createReport(@RequestBody Report report) {
		logger.info("Request received: POST /attendanceReports/add - Request Body: {}", report);
		Report createdReport = reportService.createReport(report);
		logger.info("Response sent: POST /attendanceReports/add - Report created with ID: {}", createdReport.getReportId());
		return createdReport;
	}

	@GetMapping("/")
	public List<Report> getAllReports() {
		logger.info("Request received: GET /attendanceReports/");
		List<Report> reports = reportService.getAllReports();
		logger.info("Response sent: GET /attendanceReports/ - Retrieved {} reports", reports.size());
		return reports;
	}

	@GetMapping("/{reportID}")
	public ResponseEntity<Report> getReportById(@PathVariable Long reportID) {
		logger.info("Request received: GET /attendanceReports/{}", reportID);
		ResponseEntity<Report> response = reportService.getReportById(reportID)
				.map(ResponseEntity::ok).orElseGet(() -> {
					logger.warn("Response sent: GET /attendanceReports/{} - Report not found", reportID);
					return ResponseEntity.notFound().build();
				});
		logger.info("Response sent: GET /attendanceReports/{}", reportID);
		return response;
	}

	@PutMapping("/{id}")
	public Report update(@PathVariable Long id, @RequestBody Report report) {
		logger.info("Request received: PUT /attendanceReports/{} - Request Body: {}", id, report);
		Report updatedReport = reportService.updateReport(id, report);
		logger.info("Response sent: PUT /attendanceReports/{} - Report updated", id);
		return updatedReport;
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		logger.info("Request received: DELETE /attendanceReports/{}", id);
		reportService.deleteReport(id);
		logger.info("Response sent: DELETE /attendanceReports/{} - Report deleted", id);
	}
	
	
	
	
    
    // Done
    @GetMapping(value = "/leave-chart-xchart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getLeavePieChartXChart() throws IOException {
        PieChart chart = reportService.generatePieChart();
        return BitmapEncoder.getBitmapBytes(chart, BitmapFormat.PNG);
    }
    
    // Done
    @GetMapping(value = "/tru-time-bar-chart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getTruTimeBarChart() throws IOException {
        return reportService.generateTimeDifferenceBarChart();
    }

    // Done
    @GetMapping(value = "/month-wise-leave-chart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getMonthWiseLeaveChart() throws IOException {
        return reportService.generateMonthWiseLeaveCountLineChart();
    }
    
    // Done
    @GetMapping(value = "/year-wise-leave-chart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getYearWiseLeaveChart() throws IOException {
        return reportService.generateYearWiseLeaveCountLineChart();
    }
    
    @GetMapping("/leave-chart-page")
    public String getLeaveChartPage() {
        return "leave-chart";
    }

    @GetMapping("/tru-time-chart-page")
    public String getTruTimeChartPage() {
        return "tru-time-chart";
    }

    @GetMapping("/month-wise-leave-page")
    public String getMonthWiseLeaveChartPage() {
        return "month-wise-leave-chart";
    }

    @GetMapping("/year-wise-leave-page")
    public String getYearWiseLeaveChartPage() {
        return "year-wise-leave-chart";
    }
}
