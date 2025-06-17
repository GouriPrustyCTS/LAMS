package com.leave.lams.dao;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors; // Correct import for XChart colors
import org.knowm.xchart.XYSeries; // Needed for setting marker color on series directly
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
import com.leave.lams.repository.LeaveRequestRepository; // Ensure this is imported
import com.leave.lams.repository.ReportRepository;
import com.leave.lams.service.ReportService;

@Service
public class ReportDAO implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportDAO.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper mapper;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository; // Correctly autowired

    @Autowired(required = false)
    private AttendanceRepository attendanceRepository;

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

            if (existing.getEmployee() == null || !existing.getEmployee().getEmployeeId().equals(report.getEmployeeId())) {
                throw new IllegalArgumentException("Employee ID does not match the owner of this record or employee information is missing.");
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

    @Override
    public List<LeaveRequest> getEmployeeLeaveReport(Long employeeId) {
        logger.info("Fetching leave report for employee ID: {}", employeeId);
        try {
            // CORRECTED: Using findByEmployee_EmployeeId as per your repository
            List<LeaveRequest> employeeLeaves = leaveRequestRepository.findByEmployee_EmployeeId(employeeId);
            if (employeeLeaves.isEmpty()) {
                logger.warn("No leave requests found for employee ID: {}", employeeId);
            }
            return employeeLeaves;
        } catch (Exception e) {
            logger.error("Error fetching leave report for employee ID {}: {}", employeeId, e.getMessage());
            throw new RuntimeException("Failed to retrieve employee leave report", e);
        }
    }

    @Override
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

    @Override
    public byte[] generateTimeDifferenceBarChart(Long empId) throws IOException {
        logger.info("Generating clock-in/clock-out time difference bar chart for employee ID: {}", empId);
        List<Map<String, Object>> timeEntries = null;
        if (attendanceRepository != null) {
            try {
                if (empId != null) {
                    timeEntries = attendanceRepository.getClockInOutDataByEmpId(empId);
                    logger.info("Retrieved clock-in/clock-out data for employee {}: {}", empId, timeEntries);
                } else {
                    timeEntries = attendanceRepository.getClockInOutData();
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
                    Long currentEmpId = ((Number) entry.get("empId")).longValue();
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
                    .width(950)
                    .height(500)
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
            logger.warn("AttendanceRepository is not available, cannot generate time difference bar chart.");
            return new byte[0];
        }
    }

    @Override
    public byte[] generateMonthWiseLeaveCountLineChart() throws IOException {
        logger.info("Generating month-wise leave count line chart...");
        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();
        Map<YearMonth, Integer> monthlyCounts = new TreeMap<>();

        for (LeaveRequest leave : allLeaves) {
            LocalDate startDate = leave.getStartDate();
            LocalDate endDate = leave.getEndDate();

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

        List<java.util.Date> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();

        for (Map.Entry<YearMonth, Integer> entry : monthlyCounts.entrySet()) {
            xData.add(java.util.Date.from(entry.getKey().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            yData.add(entry.getValue().doubleValue());
        }

        XYChart chart = new XYChartBuilder()
                .width(950)
                .height(500)
                .title("Month-wise Leave Count")
                .xAxisTitle("Month/Year")
                .yAxisTitle("Number of Leaves")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setDatePattern("yyyy-MM");
        chart.getStyler().setMarkerSize(8);

        // --- Start of Month-wise Chart Color Customization ---
        // Series colors are set on the styler for the overall chart appearance
        chart.getStyler().setSeriesColors(new java.awt.Color[]{XChartSeriesColors.BLUE.getColor(null)}); // Data line color

        chart.getStyler().setChartBackgroundColor(java.awt.Color.decode("#F0F8FF")); // Alice Blue
        chart.getStyler().setPlotBackgroundColor(java.awt.Color.decode("#E6F2FF")); // Lighter Blue
        chart.getStyler().setPlotGridLinesVisible(true);

        // CORRECTED: Use setPlotGridLinesColor for grid line color
        chart.getStyler().setPlotGridLinesColor(java.awt.Color.GRAY); // Grid line color

        chart.getStyler().setAxisTickLabelsFont(new Font("Arial", Font.PLAIN, 10));
        chart.getStyler().setAxisTitleFont(new Font("Arial", Font.BOLD, 12));
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 14));
        chart.getStyler().setAxisTickLabelsColor(java.awt.Color.DARK_GRAY);
        chart.getStyler().setChartTitleBoxBackgroundColor(java.awt.Color.decode("#ADD8E6")); // Light Blue for title box
        chart.getStyler().setChartTitleBoxVisible(true);
        chart.getStyler().setChartTitleBoxBorderColor(java.awt.Color.BLUE);
        // --- End of Month-wise Chart Color Customization ---

        if (!xData.isEmpty() && !yData.isEmpty()) {
            XYSeries series = chart.addSeries("Leave Count", xData, yData);
            // CORRECTED: Set marker color directly on the series if different from line color
            series.setMarkerColor(XChartSeriesColors.BLUE.getColor(null)); // Example: same as line color
        } else {
            logger.warn("No monthly leave data found to populate the line chart. Adding 'No Data' series.");
            XYSeries noDataSeries = chart.addSeries("No Data", new ArrayList<java.util.Date>(), new ArrayList<Double>());
            noDataSeries.setLineColor(java.awt.Color.DARK_GRAY);
            // CORRECTED: Set marker color directly on the 'No Data' series
            noDataSeries.setMarkerColor(java.awt.Color.DARK_GRAY);
            chart.setTitle("Month-wise Leave Count (No Data Available)");
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, os, BitmapFormat.PNG);
        return os.toByteArray();
    }

    @Override
    public byte[] generateYearWiseLeaveCountLineChart() throws IOException {
        logger.info("Generating year-wise leave count line chart...");
        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();
        Map<Integer, Integer> yearlyCounts = new TreeMap<>();

        for (LeaveRequest leave : allLeaves) {
            LocalDate startDate = leave.getStartDate();
            LocalDate endDate = leave.getEndDate();

            if (startDate != null && endDate != null) {
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    yearlyCounts.put(currentDate.getYear(), yearlyCounts.getOrDefault(currentDate.getYear(), 0) + 1);
                    currentDate = currentDate.plusDays(1);
                }
            } else if (startDate != null) {
                int year = startDate.getYear();
                yearlyCounts.put(year, yearlyCounts.getOrDefault(year, 0) + 1);
                logger.info("Incrementing year {} count (start date only)", year);
            }
        }

        logger.info("Final yearlyCounts: {}", yearlyCounts);

        List<Double> years = new ArrayList<>();
        List<Double> counts = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : yearlyCounts.entrySet()) {
            years.add(entry.getKey().doubleValue());
            counts.add(entry.getValue().doubleValue());
        }

        XYChart chart = new XYChartBuilder()
                .width(950)
                .height(500)
                .title("Year-wise Leave Count")
                .xAxisTitle("Year")
                .yAxisTitle("Number of Leaves")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLabelRotation(0);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setMarkerSize(8);

        // --- Start of Year-wise Chart Color Customization ---
        chart.getStyler().setSeriesColors(new java.awt.Color[]{XChartSeriesColors.MAGENTA.getColor(null)}); // Data line color

        chart.getStyler().setChartBackgroundColor(java.awt.Color.decode("#FFF5EE")); // SeaShell
        chart.getStyler().setPlotBackgroundColor(java.awt.Color.decode("#F5F5DC")); // Beige
        chart.getStyler().setPlotGridLinesVisible(true);

        // CORRECTED: Use setPlotGridLinesColor for grid line color
        chart.getStyler().setPlotGridLinesColor(java.awt.Color.DARK_GRAY); // Grid line color

        chart.getStyler().setAxisTickLabelsFont(new Font("Arial", Font.PLAIN, 10));
        chart.getStyler().setAxisTitleFont(new Font("Arial", Font.BOLD, 12));
        chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 14));
        chart.getStyler().setAxisTickLabelsColor(java.awt.Color.BLACK);
        chart.getStyler().setChartTitleBoxBackgroundColor(java.awt.Color.decode("#FFE4B5")); // Moccasin
        chart.getStyler().setChartTitleBoxVisible(true);
        chart.getStyler().setChartTitleBoxBorderColor(java.awt.Color.ORANGE);
        // --- End of Year-wise Chart Color Customization ---

        if (!years.isEmpty() && !counts.isEmpty()) {
            XYSeries series = chart.addSeries("Leave Count", years, counts);
            // CORRECTED: Set marker color directly on the series if different from line color
            series.setMarkerColor(XChartSeriesColors.MAGENTA.getColor(null)); // Example: same as line color
        } else {
            logger.warn("No yearly leave data found to populate the line chart. Adding 'No Data' series.");
            XYSeries noDataSeries = chart.addSeries("No Data", new double[]{0}, new double[]{0});
            noDataSeries.setLineColor(java.awt.Color.DARK_GRAY);
            // CORRECTED: Set marker color directly on the 'No Data' series
            noDataSeries.setMarkerColor(java.awt.Color.DARK_GRAY);
            chart.setTitle("Year-wise Leave Count (No Data Available)");
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapEncoder.saveBitmap(chart, os, BitmapFormat.PNG);
        return os.toByteArray();
    }
}