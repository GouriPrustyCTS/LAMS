package com.leave.model;

import java.util.Date;
import java.time.LocalTime;

public class Shift {
    private int shiftID;
    private Date shiftDate;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private int employeeID;

   

    public Shift() {
    }

    public Shift(int shiftID, Date shiftDate, LocalTime shiftStartTime, LocalTime shiftEndTime, int employeeID) {
        this.shiftID = shiftID;
        this.shiftDate = shiftDate;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
        this.employeeID = employeeID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(LocalTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(LocalTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    @Override
    public String toString() {
        return "Shift{" +
               "shiftID=" + shiftID +
               ", shiftDate=" + shiftDate +
               ", shiftStartTime=" + shiftStartTime +
               ", shiftEndTime=" + shiftEndTime +
               ", employeeID=" + employeeID +
               '}';
    }
}