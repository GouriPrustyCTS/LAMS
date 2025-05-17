package com.leave.lams.model;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_balance")
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveBalanceId;
    
    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
//    @JsonBackReference
    @JsonIncludeProperties({"employeeId","name"})
    private Employee employee;
    
    private String leaveType;
    private Double balance;
}
