package com.leave.model;
import java.util.Date;
import java.time.LocalDateTime;

public class LeaveRequest {
    private int leaveRequestID;
    private int employeeID;
    private Date startDate;
    private Date endDate;
    private LocalDateTime requestDate;
    private String reason;
    private String leaveType;
    private String status;

    

    public LeaveRequest() {
    }

    public LeaveRequest(int leaveRequestID, int employeeID, Date startDate, Date endDate, LocalDateTime requestDate, String reason, String leaveType, String status) {
        this.leaveRequestID = leaveRequestID;
        this.employeeID = employeeID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestDate = requestDate;
        this.reason = reason;
        this.leaveType = leaveType;
        this.status = status;
    }

    public int getLeaveRequestID() {
        return leaveRequestID;
    }

    public void setLeaveRequestID(int leaveRequestID) {
        this.leaveRequestID = leaveRequestID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    @Override
    public String toString() {
        return "LeaveRequest{" +
               "leaveRequestID=" + leaveRequestID +
               ", employeeID=" + employeeID +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", requestDate=" + requestDate +
               ", reason='" + reason + '\'' +
               ", leaveType='" + leaveType + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}