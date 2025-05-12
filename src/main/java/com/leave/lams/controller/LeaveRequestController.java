package com.leave.lams.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.leave.lams.model.LeaveRequest;
import com.leave.lams.repository.LeaveRequestRepository;
import com.leave.lams.service.LeaveRequestService;

@RestController
@RequestMapping("/leaveRequests")
public class LeaveRequestController {

	private static final Logger logger = LoggerFactory.getLogger(LeaveRequestController.class);

	@Autowired
	private LeaveRequestService leaveRequestService;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	@PostMapping("/add")
	public LeaveRequest createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
		logger.info("Request received: POST /leaveRequests/add - Request Body: {}", leaveRequest);
		LeaveRequest createdRequest = leaveRequestService.createLeaveRequest(leaveRequest);
		if (createdRequest != null) {
			logger.info("Response sent: POST /leaveRequests/add - LeaveRequest created with ID: {}", createdRequest.getId());
		} else {
			logger.warn("Response sent: POST /leaveRequests/add - LeaveRequest add failed.");
		}
		return createdRequest;
	}

	@GetMapping("/")
	public List<LeaveRequest> getAllLeaveRequests() {
		logger.info("Request received: GET /leaveRequests/");
		List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();
		logger.info("Response sent: GET /leaveRequests/ - Retrieved {} leave requests", leaveRequests.size());
		return leaveRequests;
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
		logger.info("Request received: GET /leaveRequests/{}", id);
		Optional<LeaveRequest> leaveRequest = leaveRequestService.getLeaveRequestById(id);
		if (leaveRequest.isPresent()) {
			logger.info("Response sent: GET /leaveRequests/{} - LeaveRequest found", id);
			return ResponseEntity.ok(leaveRequest.get());
		} else {
			logger.warn("Response sent: GET /leaveRequests/{} - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<LeaveRequest> updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequest updatedRequest) {
		logger.info("Request received: PUT /leaveRequests/{} - Request Body: {}", id, updatedRequest);
		LeaveRequest leaveRequest = leaveRequestService.updateLeaveRequest(id, updatedRequest);
		if (leaveRequest == null) {
			logger.warn("Response sent: PUT /leaveRequests/{} - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
		logger.info("Response sent: PUT /leaveRequests/{} - LeaveRequest updated", id);
		return ResponseEntity.ok(leaveRequest);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
		logger.info("Request received: DELETE /leaveRequests/{}", id);
		boolean res = leaveRequestService.deleteLeaveRequest(id);
		if (!res) {
			logger.warn("Response sent: DELETE /leaveRequests/{} - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
		logger.info("Response sent: DELETE /leaveRequests/{} - LeaveRequest deleted", id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<LeaveRequest> updateLeaveStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
		logger.info("Request received: PATCH /leaveRequests/{}/status - Request Body: {}", id, body);
		String newStatus = body.get("status");
		LeaveRequest updated = leaveRequestService.updateLeaveStatus(id, newStatus);
		if (updated == null) {
			logger.warn("Response sent: PATCH /leaveRequests/{}/status - LeaveRequest not found", id);
			return ResponseEntity.notFound().build();
		}
		logger.info("Response sent: PATCH /leaveRequests/{}/status - LeaveRequest status updated to {}", id, newStatus);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/employee/{employeeId}")
	public List<LeaveRequest> getRequestsByEmployee(@PathVariable Long employeeId) {
		logger.info("Request received: GET /leaveRequests/employee/{}", employeeId);
		List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeEmployeeId(employeeId);
		logger.info("Response sent: GET /leaveRequests/employee/{} - Retrieved {} leave requests", employeeId, leaveRequests.size());
		return leaveRequests;
	}
	
//	_____________________________________________________________________________________________________________________________________
	
//  Handles the request for the leave report download page
    @GetMapping("/leave-report-page")
    public String showLeaveReportPage(Model model) {
        logger.info("Accessing leave report page.");
        List<LeaveRequest> leaveReportData = leaveRequestService.getLeaveReportDataSortedByMonth();
        model.addAttribute("leaveReportData", leaveReportData);
        return "leave_report_download";
    }

    @GetMapping("/downloadLeaveReportXcel")
    public ResponseEntity<byte[]> downloadLeaveReportSortedByMonth() {
        logger.info("Starting to generate leave report.");
        try {
            List<LeaveRequest> leaveReportData = leaveRequestService.getLeaveReportDataSortedByMonth();

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
				for (LeaveRequest leave : leaveReportData) {
				    Row row = sheet.createRow(rowNum++);
				    row.createCell(0).setCellValue(leave.getEmployee().getEmployeeId());
				    row.createCell(1).setCellValue(leave.getEmployee().getName());
				    row.createCell(2).setCellValue(leave.getReason());
				    if (leave.getEndDate() != null) {
				        row.createCell(3).setCellValue(dateFormat.format(leave.getEndDate()));
				    }
				    row.createCell(4).setCellValue(leave.getLeaveType());
				    if (leave.getStartDate() != null) {
				        row.createCell(5).setCellValue(dateFormat.format(leave.getStartDate()));
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
            List<LeaveRequest> leaveReportData = leaveRequestService.getLeaveReportDataSortedByMonth();

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
            for (LeaveRequest leave : leaveReportData) {
                table.addCell(new Paragraph(Long.toString(leave.getEmployee().getEmployeeId()), cellFont));
                table.addCell(new Paragraph(leave.getEmployee().getName(), cellFont));
                table.addCell(new Paragraph(leave.getReason(), cellFont));
                if (leave.getEndDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(leave.getEndDate()), cellFont));
                } else {
                    table.addCell(new Paragraph("", cellFont));
                }
                table.addCell(new Paragraph(leave.getLeaveType(), cellFont));
                if (leave.getStartDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(leave.getStartDate()), cellFont));
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
            List<LeaveRequest> leaveReportData = leaveRequestService.getLeaveDetailsByEmployee(empId);
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
            List<LeaveRequest> leaveReportData = leaveRequestService.getLeaveDetailsByEmployee(empId);

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
            for (LeaveRequest leave : leaveReportData) {
                table.addCell(new Paragraph(Long.toString(leave.getEmployee().getEmployeeId()), cellFont));
                table.addCell(new Paragraph(leave.getEmployee().getName(), cellFont));
                table.addCell(new Paragraph(leave.getReason(), cellFont));
                if (leave.getEndDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(leave.getEndDate()), cellFont));
                } else {
                    table.addCell(new Paragraph("", cellFont));
                }
                table.addCell(new Paragraph(leave.getLeaveType(), cellFont));
                if (leave.getStartDate() != null) {
                    table.addCell(new Paragraph(dateFormat.format(leave.getStartDate()), cellFont));
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
