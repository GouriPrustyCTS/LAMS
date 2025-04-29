package com.leave.model;

import java.util.Date;
import java.time.LocalDateTime;

public class Report {
    private int reportID;
    private Date dateRangeStart;
    private Date dateRangeEnd;
    private LocalDateTime generatedDate;
    private String reportData;
    private int employeeID;
    private String reportType;

    

    public Report() {
    }

    public Report(int reportID, Date dateRangeStart, Date dateRangeEnd, LocalDateTime generatedDate, String reportData, int employeeID, String reportType) {
        this.reportID = reportID;
        this.dateRangeStart = dateRangeStart;
        this.dateRangeEnd = dateRangeEnd;
        this.generatedDate = generatedDate;
        this.reportData = reportData;
        this.employeeID = employeeID;
        this.reportType = reportType;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public Date getDateRangeStart() {
        return dateRangeStart;
    }

    public void setDateRangeStart(Date dateRangeStart) {
        this.dateRangeStart = dateRangeStart;
    }

    public Date getDateRangeEnd() {
        return dateRangeEnd;
    }

    public void setDateRangeEnd(Date dateRangeEnd) {
        this.dateRangeEnd = dateRangeEnd;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return "Report{" +
               "reportID=" + reportID +
               ", dateRangeStart=" + dateRangeStart +
               ", dateRangeEnd=" + dateRangeEnd +
               ", generatedDate=" + generatedDate +
               ", reportData='" + reportData + '\'' +
               ", employeeID=" + employeeID +
               ", reportType='" + reportType + '\'' +
               '}';
    }
}