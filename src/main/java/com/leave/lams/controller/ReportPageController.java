package com.leave.lams.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.leave.lams.dao.ReportDAO;
import com.leave.lams.dto.LeaveRequestDTO;
import com.leave.lams.service.LeaveRequestService;
import com.leave.lams.service.ReportService;

@Controller
@RequestMapping("/reports")
public class ReportPageController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportPageController.class);
	@Autowired
	private ReportDAO reportDAO;
	@Autowired
	private LeaveRequestService leaveRequestService;
	
	@Autowired
	private ReportService reportService;
	
	
	@GetMapping("/index")
    public String index() {
        return "index"; // matches index.html in /templates
    }
	
//	___________________________________________________________________________________________
	
	 // Done
    @GetMapping(value = "/leave-chart-xchart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getLeavePieChartXChart() throws IOException {
        PieChart chart = reportService.generatePieChart();
        return BitmapEncoder.getBitmapBytes(chart, BitmapFormat.PNG);
    }
    
    @GetMapping(value = "/tru-time-bar-chart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getTruTimeBarChart(@RequestParam(value = "empId", required = false) Long empId) throws IOException {
        return reportService.generateTimeDifferenceBarChart(empId);
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
    public String getTruTimeChartPage(@RequestParam(value = "empId", required = false) Long empId, Model model) throws IOException {
        byte[] chartBytes;
        if (empId != null) {
            chartBytes = reportDAO.generateTimeDifferenceBarChart(empId);
            model.addAttribute("chartData", Base64.getEncoder().encodeToString(chartBytes));
            model.addAttribute("filteredByEmpId", empId); // Optional: To display that it's filtered
        } else {
            chartBytes = reportDAO.generateTimeDifferenceBarChart(null); // Pass null to get all data
            model.addAttribute("chartData", Base64.getEncoder().encodeToString(chartBytes));
        }
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
    
//    _____________________________________________________________________________________________

	
//  Handles the request for the leave report download page
    @GetMapping("/leave-report-page")
    public String showLeaveReportPage(Model model) {
        logger.info("Accessing leave report page.");
        List<LeaveRequestDTO> leaveReportData = leaveRequestService.getLeaveReportDataSortedByMonth();
        model.addAttribute("leaveReportData", leaveReportData);
        return "leave_report_download";
    }

    @GetMapping("/downloadLeaveReportXcel")
    public ResponseEntity<byte[]> downloadLeaveReportSortedByMonth() {
        logger.info("Starting to generate leave report.");
        try {
            List<LeaveRequestDTO> leaveReportData = leaveRequestService.getLeaveReportDataSortedByMonth();

            try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Leave Details Sorted By Month");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				// Create header row
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("Employee ID");
				headerRow.createCell(1).setCellValue("Employee Name");
				headerRow.createCell(2).setCellValue("Reason");
				headerRow.createCell(3).setCellValue("End Date");
				headerRow.createCell(4).setCellValue("Leave Type");
				headerRow.createCell(5).setCellValue("Start Date");
				headerRow.createCell(6).setCellValue("Status");

				// Populate data rows
				int rowNum = 1;
				for (LeaveRequestDTO leave : leaveReportData) {
				    Row row = sheet.createRow(rowNum++);
				    row.createCell(0).setCellValue(leave.getEmployeeId());
				    row.createCell(1).setCellValue(leave.getName());
				    row.createCell(2).setCellValue(leave.getReason());
				    if (leave.getEndDate() != null) {
				        row.createCell(3).setCellValue(dateFormat.format(java.sql.Date.valueOf(leave.getEndDate())));
				    }
				    row.createCell(4).setCellValue(leave.getLeaveType());
				    if (leave.getStartDate() != null) {
				        row.createCell(5).setCellValue(dateFormat.format(java.sql.Date.valueOf(leave.getStartDate())));
				    }
				    row.createCell(6).setCellValue(leave.getStatus());
				}

				// Write the workbook content to a ByteArrayOutputStream
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);

				// Set response headers for file download
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.setContentDispositionFormData("attachment", "leave_report_sorted_by_month.xlsx");

				logger.info("Leave report generated successfully.");
				return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
			}

        } catch (IOException e) {
            logger.error("Error generating leave report: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/downloadLeaveReportPdf")
    public ResponseEntity<byte[]> downloadLeaveReportPdf() {
        logger.info("Starting to generate leave report PDF.");
        try {
            List<LeaveRequestDTO> leaveReportData = leaveRequestService.getLeaveReportDataSortedByMonth();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Define fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);

            // Add title
            Paragraph title = new Paragraph("Leave Report", titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            table.addCell(new Paragraph("Employee ID", headerFont));
            table.addCell(new Paragraph("Employee Name", headerFont));
            table.addCell(new Paragraph("Reason", headerFont));
            table.addCell(new Paragraph("End Date", headerFont));
            table.addCell(new Paragraph("Leave Type", headerFont));
            table.addCell(new Paragraph("Start Date", headerFont));
            table.addCell(new Paragraph("Status", headerFont));

            // Add table data
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (LeaveRequestDTO leave : leaveReportData) {
                table.addCell(new Paragraph(Long.toString(leave.getEmployeeId()), cellFont));
                table.addCell(new Paragraph(leave.getName(), cellFont));
                table.addCell(new Paragraph(leave.getReason(), cellFont));
                if (leave.getEndDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(java.sql.Date.valueOf(leave.getEndDate())), cellFont));
                } else {
                    table.addCell(new Paragraph("", cellFont));
                }
                table.addCell(new Paragraph(leave.getLeaveType(), cellFont));
                if (leave.getStartDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(java.sql.Date.valueOf(leave.getStartDate())), cellFont));
                } else {
                    table.addCell(new Paragraph("", cellFont));
                }
                table.addCell(new Paragraph(leave.getStatus(), cellFont));
            }

            document.add(table);
            document.close();

            // Set response headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "leave_report_sorted_by_month.pdf");

            logger.info("Leave report PDF generated successfully.");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (DocumentException e) {
            logger.error("Error generating leave report PDF: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New method to display employee leave records without download options
    @GetMapping("/myLeaveRecords/{empId}") // myLeaveRecords?empId=23
    public String showMyLeaveRecords(@PathVariable Long empId, Model model) {
        logger.info("Entering showMyLeaveRecords with empId: {}", empId); // Added logging
        try {
            List<LeaveRequestDTO> leaveReportData = leaveRequestService.getLeaveDetailsByEmployee(empId);
            model.addAttribute("leaveReportData", leaveReportData);
            model.addAttribute("empId", empId);
            logger.info("Successfully retrieved leave records for empId: {}", empId);
            return "my_leave_records";
        } catch (Exception e) {
            logger.error("Error in showMyLeaveRecords: {}", e.getMessage());
            return "error"; // Or handle the error appropriately
        }
    }

    
    @GetMapping("/downloadEmployeeLeaveReportPdf/{empId}")
    public ResponseEntity<byte[]> downloadEmployeeLeaveReportPdf(@PathVariable Long empId) {
        logger.info("Starting to generate employee leave report PDF for empId: {}", empId);
        try {
            List<LeaveRequestDTO> leaveReportData = leaveRequestService.getLeaveDetailsByEmployee(empId);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Define fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);

            // Add title
            Paragraph title = new Paragraph("Employee Leave Report", titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Employee ID: " + empId, cellFont));
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            table.addCell(new Paragraph("Employee ID", headerFont));
            table.addCell(new Paragraph("Employee Name", headerFont));
            table.addCell(new Paragraph("Reason", headerFont));
            table.addCell(new Paragraph("End Date", headerFont));
            table.addCell(new Paragraph("Leave Type", headerFont));
            table.addCell(new Paragraph("Start Date", headerFont));
            table.addCell(new Paragraph("Status", headerFont));

            // Add table data
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (LeaveRequestDTO leave : leaveReportData) {
                table.addCell(new Paragraph(Long.toString(leave.getEmployeeId()), cellFont));
                table.addCell(new Paragraph(leave.getName(), cellFont));
                table.addCell(new Paragraph(leave.getReason(), cellFont));
                if (leave.getEndDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(java.sql.Date.valueOf(leave.getEndDate())), cellFont));
                } else {
                    table.addCell(new Paragraph("", cellFont));
                }
                table.addCell(new Paragraph(leave.getLeaveType(), cellFont));
                if (leave.getStartDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(java.sql.Date.valueOf(leave.getStartDate())), cellFont));
                } else {
                    table.addCell(new Paragraph("", cellFont));
                }
                table.addCell(new Paragraph(leave.getStatus(), cellFont));
            }

            document.add(table);
            document.close();

            // Set response headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "employee_leave_report_" + empId + ".pdf");
            logger.info("Employee leave report PDF generated successfully for empId: {}", empId);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (DocumentException e) {
            logger.error("Error generating employee leave report PDF for empId: {}: {}", empId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
