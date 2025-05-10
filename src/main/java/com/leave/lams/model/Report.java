package com.leave.lams.model;

import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    
    private Date dateRangeStart;
    private Date dateRangeEnd;
    private LocalDateTime generatedDate;
    private Integer totalAttendance;
    private Integer absenteesim;
    
    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
//    @JsonBackReference
    @JsonIncludeProperties({"employeeId"})
    private Employee employee;
}
