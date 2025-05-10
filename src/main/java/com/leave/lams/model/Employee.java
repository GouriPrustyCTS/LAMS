package com.leave.lams.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeId;

	private Date hireDate;
	private String name;
	private String email;
	private String department;
	private String jobTitle;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
	private List<Attendance> attendances;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
	private List<LeaveRequest> leaveRequests;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
	private List<LeaveBalance> leaveBalances;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
	private List<Shift> shifts;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
	private List<Report> reports;
}
/*
 * "2025-06-01" "2025-05-07T09:00:00"
 */
