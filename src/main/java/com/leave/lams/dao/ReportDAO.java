package com.leave.lams.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave.lams.model.Report;
import com.leave.lams.repository.ReportRepository;
import com.leave.lams.service.ReportService;

import java.util.List;
import java.util.Optional;

@Service
public class ReportDAO implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	public Report createReport(Report report) {
		return reportRepository.save(report);
	}

	public List<Report> getAllReports() {
		return reportRepository.findAll();
	}

	public Optional<Report> getReportById(Long reportID) {
		return reportRepository.findById(reportID);
	}

	@Override
	public Report updateReport(Long id, Report report) {
		Optional<Report> existing = reportRepository.findById(id);
		if (existing.isPresent()) {
			Report r = existing.get();
			
			if(!r.getEmployee().getEmployeeId().equals(report.getEmployee().getEmployeeId())) {
				throw new IllegalArgumentException("Employee ID does not match the owner of this record.");
			}
			
			r.setDateRangeStart(report.getDateRangeStart());
			r.setDateRangeEnd(report.getDateRangeEnd());
			r.setGeneratedDate(report.getGeneratedDate());
			r.setTotalAttendance(report.getTotalAttendance());
			r.setAbsenteesim(report.getAbsenteesim());
			return reportRepository.save(r);
		}
		return null;
	}

	@Override
	public void deleteReport(Long id) {
		reportRepository.deleteById(id);
	}
}
