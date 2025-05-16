package com.leave.lams.dao;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.Styler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.dto.ReportDTO;
import com.leave.lams.exception.ReportNotFoundException;
import com.leave.lams.mapper.ReportMapper;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.model.Report;
import com.leave.lams.repository.AttendanceRepository;
import com.leave.lams.repository.LeaveRequestRepository;
import com.leave.lams.repository.ReportRepository;
import com.leave.lams.service.ReportService;

@Service
public class ReportDAO implements ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportDAO.class);

	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired 
	private ReportMapper mapper;

	@Override
    public ReportDTO createReport(ReportDTO reportDto) {
        try {
            Report report = mapper.toEntity(reportDto);
            Report savedReport = reportRepository.save(report);
            return mapper.toDTo(savedReport);
        } catch (Exception e) {
            logger.error("Failed to create report: {}", e.getMessage());
            throw new RuntimeException("Error while creating report", e);
        }
    }

    @Override
    public List<ReportDTO> getAllReports() {
        try {
            List<Report> reports = reportRepository.findAll();
            return reports.stream().map(mapper::toDTo).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to fetch reports: {}", e.getMessage());
            throw new RuntimeException("Error while fetching all reports", e);
        }
    }

    @Override
    public Optional<ReportDTO> getReportById(Long reportID) {
        try {
            Optional<Report> reportOpt = reportRepository.findById(reportID);
            if (reportOpt.isPresent()) {
                return Optional.of(mapper.toDTo(reportOpt.get()));
            } else {
                throw new ReportNotFoundException("Report not found with ID: " + reportID);
            }
        } catch (ReportNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch report with ID {}: {}", reportID, e.getMessage());
            throw new RuntimeException("Error while fetching report by ID", e);
        }
    }

    @Override
    public ReportDTO updateReport(Long id, ReportDTO report) {
        try {
            Report existing = reportRepository.findById(id)
                    .orElseThrow(() -> new ReportNotFoundException("Report not found with ID: " + id));

            if (!existing.getEmployee().getEmployeeId().equals(report.getEmployeeId())) {
                throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
            }

            existing.setDateRangeStart(report.getDateRangeStart());
            existing.setDateRangeEnd(report.getDateRangeEnd());
            existing.setGeneratedDate(report.getGeneratedDate());
            existing.setTotalAttendance(report.getTotalAttendance());
            existing.setAbsenteesim(report.getAbsenteesim());

            return mapper.toDTo(reportRepository.save(existing));
        } catch (ReportNotFoundException | IllegalArgumentException e) {
            logger.warn("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating report with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update report", e);
        }
    }

    @Override
    public void deleteReport(Long id) {
        try {
            if (!reportRepository.existsById(id)) {
                throw new ReportNotFoundException("Cannot delete. Report not found with ID: " + id);
            }
            reportRepository.deleteById(id);
        } catch (ReportNotFoundException e) {
            logger.warn("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while deleting report with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete report", e);
        }
    }
	
//	______________________________________________________________________________________________________________
	
	
	 @Autowired
	    private LeaveRequestRepository leaveRequestRepository;

	    @Autowired(required = false) // Make it not required
	    private AttendanceRepository attendanceRepository;

	    public PieChart generatePieChart() {
	        logger.info("Generating pie chart...");
	        List<Map<String, Object>> leaveCounts = leaveRequestRepository.countLeaveReasons();
	        logger.info("Leave counts retrieved: {}", leaveCounts);

	        PieChart chart = new PieChartBuilder().title("Employee Leave Reasons").build();

	        if (leaveCounts != null && !leaveCounts.isEmpty()) {
	            for (Map<String, Object> leaveData : leaveCounts) {
	                String reason = (String) leaveData.get("reason");
	                Number count = (Number) leaveData.get("count");
	                if (reason != null && count != null) {
	                    logger.info("Adding slice - Reason: {}, Count: {}", reason, count);
	                    chart.addSeries(reason, count);
	                } else {
	                    logger.warn("Skipping invalid leave data: reason={}, count={}", reason, count);
	                }
	            }
	        } else {
	            logger.warn("No leave data found to populate the chart.");
	        }

	        return chart;
	    }

	    public byte[] generateTimeDifferenceBarChart(Long empId) throws IOException {
	        logger.info("Generating clock-in/clock-out time difference bar chart for employee ID: {}", empId);
	        List<Map<String, Object>> timeEntries = null;
	        if (attendanceRepository != null) {
	            try {
	                if (empId != null) {
	                    timeEntries = attendanceRepository.getClockInOutDataByEmpId(empId); // Assuming you have this method
	                    logger.info("Retrieved clock-in/clock-out data for employee {}: {}", empId, timeEntries);
	                } else {
	                    timeEntries = attendanceRepository.getClockInOutData(); // Fetch all data if no empId is provided
	                    logger.info("Retrieved all clock-in/clock-out data: {}", timeEntries);
	                }
	            } catch (Exception e) {
	                logger.error("Error fetching clock-in/clock-out data: {}", e.getMessage());
	                timeEntries = new ArrayList<>();
	            }

	            List<String> employeeAndDate = new ArrayList<>();
	            List<Double> timeDifferencesInHours = new ArrayList<>();
	            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	            if (timeEntries != null && !timeEntries.isEmpty()) {
	                for (Map<String, Object> entry : timeEntries) {
	                    Long currentEmpId = (Long) entry.get("empId");
	                    String empName = (String) entry.get("empName");
	                    LocalDateTime clockInTime = (LocalDateTime) entry.get("clockInTime");
	                    LocalDateTime clockOutTime = (LocalDateTime) entry.get("clockOutTime");
	                    LocalDate date = (LocalDate) entry.get("date");

	                    if (clockInTime != null && clockOutTime != null && date != null && currentEmpId != null && empName != null) {
	                        java.time.Duration duration = java.time.Duration.between(clockInTime, clockOutTime);
	                        double diffInHours = (double) duration.toMinutes() / 60.0;
	                        employeeAndDate.add(empName + " (" + currentEmpId + ")\n" + date.format(dateFormatter));
	                        timeDifferencesInHours.add(diffInHours);
	                    } else {
	                        logger.warn("Incomplete clock-in/clock-out data found: {}", entry);
	                    }
	                }
	            } else {
	                logger.warn("No clock-in/clock-out data found for employee ID: {}", empId);
	            }

	            CategoryChart chart = new CategoryChartBuilder()
	                    .width(800)
	                    .height(600)
	                    .title("Time Spent at Office")
	                    .xAxisTitle("Employee (ID) - Date")
	                    .yAxisTitle("Time (Hours)")
	                    .build();
	            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
	            chart.getStyler().setOverlapped(true);
	            chart.getStyler().setXAxisLabelRotation(45);

	            if (!employeeAndDate.isEmpty() && !timeDifferencesInHours.isEmpty()) {
	                chart.addSeries("Time Spent", employeeAndDate, timeDifferencesInHours);
	            }

	            return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
	        } else {
	            logger.warn("TruTimeRepository is not available, cannot generate time difference bar chart.");
	            return new byte[0]; // Return an empty byte array or handle as needed
	        }
	    }

	    public byte[] generateMonthWiseLeaveCountLineChart() throws IOException {
	        logger.info("Generating month-wise leave count line chart...");
	        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();
	        Map<YearMonth, Integer> monthlyCounts = new TreeMap<>();
	        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

	        for (LeaveRequest leave : allLeaves) {
	            LocalDate startDate = (LocalDate) leave.getStartDate();
	            LocalDate endDate = (LocalDate) leave.getEndDate();

	            logger.info("Processing leave for month-wise - Start Date: {}, End Date: {}", startDate, endDate);

	            if (startDate != null && endDate != null) {
	                LocalDate currentDate = startDate;
	                while (!currentDate.isAfter(endDate)) {
	                    YearMonth yearMonth = YearMonth.from(currentDate);
	                    monthlyCounts.put(yearMonth, monthlyCounts.getOrDefault(yearMonth, 0) + 1);
	                    currentDate = currentDate.plusDays(1);
	                }
	            } else if (startDate != null) {
	                YearMonth yearMonth = YearMonth.from(startDate);
	                monthlyCounts.put(yearMonth, monthlyCounts.getOrDefault(yearMonth, 0) + 1);
	            }
	        }

	        logger.info("Final monthlyCounts: {}", monthlyCounts);

	        List<String> months = new ArrayList<>();
	        List<Integer> counts = new ArrayList<>();
	        for (Map.Entry<YearMonth, Integer> entry : monthlyCounts.entrySet()) {
	            months.add(entry.getKey().format(yearMonthFormatter));
	            counts.add(entry.getValue());
	        }

	        CategoryChart chart = new CategoryChartBuilder()
	                .width(800)
	                .height(600)
	                .title("Month-wise Leave Count")
	                .xAxisTitle("Year-Month")
	                .yAxisTitle("Number of Leaves")
	                .build();
	        chart.getStyler().setLegendVisible(false);
	        chart.getStyler().setXAxisLabelRotation(45);

	        if (!months.isEmpty() && !counts.isEmpty()) {
	            chart.addSeries("Leave Count", months, counts);
	            return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
	        } else {
	            logger.warn("No monthly leave data found to populate the line chart.");
	            // Return a default image or handle the empty chart scenario as needed.
	            return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG); // Return empty chart.
	        }
	    }

	    public byte[] generateYearWiseLeaveCountLineChart() throws IOException {
	        logger.info("Generating year-wise leave count line chart...");
	        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();
	        Map<Integer, Integer> yearlyCounts = new TreeMap<>();

	        for (LeaveRequest leave : allLeaves) {
	            LocalDate startDate = leave.getStartDate();
	            LocalDate endDate = leave.getEndDate();

	            logger.info("Processing leave for year-wise - Start Date: {}, End Date: {}", startDate, endDate);

	            if (startDate != null && endDate != null) {
	                int startYear = startDate.getYear();
	                int endYear = endDate.getYear();
	                for (int year = startYear; year <= endYear; year++) {
	                    yearlyCounts.put(year, yearlyCounts.getOrDefault(year, 0) + 1);
	                    logger.info("Incrementing year {} count", year);
	                }
	            } else if (startDate != null) {
	                int year = startDate.getYear();
	                yearlyCounts.put(year, yearlyCounts.getOrDefault(year, 0) + 1);
	                logger.info("Incrementing year {} count (start date only)", year);
	            }
	        }

	        logger.info("Final yearlyCounts: {}", yearlyCounts);

	        List<Integer> years = new ArrayList<>();
	        List<Integer> counts = new ArrayList<>();
	        for (Map.Entry<Integer, Integer> entry : yearlyCounts.entrySet()) {
	            years.add(entry.getKey());
	            counts.add(entry.getValue());
	        }

	        CategoryChart chart = new CategoryChartBuilder()
	                .width(800)
	                .height(600)
	                .title("Year-wise Leave Count")
	                .xAxisTitle("Year")
	                .yAxisTitle("Number of Leaves")
	                .build();
	        chart.getStyler().setLegendVisible(false);

	        if (!years.isEmpty() && !counts.isEmpty()) {
	            chart.addSeries("Leave Count", years, counts);
	            return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
	        } else {
	            logger.warn("No yearly leave data found to populate the line chart.");
	            return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG); // Complete return statement
	        }
	    }

	    public List<LeaveRequest> getEmployeeLeaveReport(Long employeeId) {
	        logger.info("Fetching leave report for employee ID: {}", employeeId);
	        List<LeaveRequest> empLeaves = leaveRequestRepository.findByEmployee_EmployeeId(employeeId);
	        if (empLeaves == null) {
	            logger.warn("No leaves found for employee ID: {}", employeeId);
	            return new ArrayList<>();
	        }
	        return empLeaves;
	    }

}
