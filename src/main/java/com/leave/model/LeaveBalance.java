package com.leave.model;
public class LeaveBalance {
    private int employeeID;
    private String leaveType;
    private double balance;


    public LeaveBalance() {
    }

    public LeaveBalance(int employeeID, String leaveType, double balance) {
        this.employeeID = employeeID;
        this.leaveType = leaveType;
        this.balance = balance;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "LeaveBalance{" +
               "employeeID=" + employeeID +
               ", leaveType='" + leaveType + '\'' +
               ", balance=" + balance +
               '}';
    }
}