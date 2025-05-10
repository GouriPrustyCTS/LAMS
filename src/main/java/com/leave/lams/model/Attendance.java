package com.leave.lams.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;
    
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private LocalDate attendanceDate;
    
    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
//    @JsonBackReference
//    @JsonIgnoreProperties({"hireDate","name","email","department","jobTitle","attendances","leaveRequests","leaveBalances","shifts","reports"})
    @JsonIncludeProperties({"employeeId"})
    private Employee employee;
    
    private Double workHours;
}
