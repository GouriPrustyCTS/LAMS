package com.leave.model;

import java.util.Date;
import java.time.LocalDateTime;

public class Attendance {
    private int attendanceID;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private Date attendanceDate;
    private int employeeID;
    private double workHours;

    public Attendance() {
    }

    public Attendance(int attendanceID, LocalDateTime clockInTime, LocalDateTime clockOutTime, Date attendanceDate, int employeeID, double workHours) {
        this.attendanceID = attendanceID;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.attendanceDate = attendanceDate;
        this.employeeID = employeeID;
        this.workHours = workHours;
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(LocalDateTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalDateTime getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(LocalDateTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    @Override
    public String toString() {
        return "Attendance{" +
               "attendanceID=" + attendanceID +
               ", clockInTime=" + clockInTime +
               ", clockOutTime=" + clockOutTime +
               ", attendanceDate=" + attendanceDate +
               ", employeeID=" + employeeID +
               ", workHours=" + workHours +
               '}';
    }
}
