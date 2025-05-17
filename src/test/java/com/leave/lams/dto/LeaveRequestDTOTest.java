package com.leave.lams.dto;

import java.time.LocalDate;

import com.leave.lams.model.LeaveRequest;

public class LeaveRequestDTOTest {
    private Long leaveRequestId;
    private String leaveType;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long employeeId;

    public LeaveRequestDTOTest() {
    }

    public LeaveRequestDTOTest(LeaveRequest leaveRequest) {
        this.leaveRequestId = leaveRequest.getLeaveRequestId();
        this.leaveType = leaveRequest.getLeaveType();
        this.status = leaveRequest.getStatus();
        this.startDate = leaveRequest.getStartDate();
        this.endDate = leaveRequest.getEndDate();
        this.employeeId = leaveRequest.getEmployee() != null ? leaveRequest.getEmployee().getEmployeeId() : null;
    }

    // Getters and Setters
    public Long getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(Long leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}