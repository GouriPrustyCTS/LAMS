package com.leave.model;


import java.util.Date;

public class Employee {
    private int employeeID;
    private Date hireDate;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String jobTitle;

    public Employee() {
    }

    public Employee(int employeeID, Date hireDate, String firstName, String lastName, String email, String department, String jobTitle) {
        this.employeeID = employeeID;
        this.hireDate = hireDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.jobTitle = jobTitle;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "Employee{" +
               "employeeID=" + employeeID +
               ", hireDate=" + hireDate +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", department='" + department + '\'' +
               ", jobTitle='" + jobTitle + '\'' +
               '}';
    }
}