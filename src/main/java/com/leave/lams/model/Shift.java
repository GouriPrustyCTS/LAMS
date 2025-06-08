package com.leave.lams.model;

import java.time.LocalTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
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
@Table(name = "shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shiftId;
    
    @Column(name = "shift_name", nullable = false) // Added and mapped to 'shift_name' column
    private String shiftName; 
    
    private Date shiftDate;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    
    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
//    @JsonBackReference
    @JsonIncludeProperties({"employeeId","name"})
    private Employee employee;
}
