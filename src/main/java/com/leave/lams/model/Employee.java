package com.leave.lams.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	@Column(unique = true)
	private String email;
	private String department;
	private String jobTitle;

	private String password;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attendance> attendances;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeaveRequest> leaveRequests;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeaveBalance> leaveBalances;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Shift> shifts;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Report> reports;

	@OneToMany(mappedBy = "fromEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<ShiftSwapRequest> sentSwapRequests;

	@OneToMany(mappedBy = "toEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<ShiftSwapRequest> recievedSwapRequests;

	// --- Custom Getters for Frontend Compatibility ---

	// This method will be used by EmployeeController.java to provide the "title"
	public String getDesignation() {
		return this.jobTitle;
	}

	// Assuming you want to expose first and last name for frontend display
	// If your 'name' field already stores "FirstName LastName", then these might
	// not be strictly necessary
	// but are good to have if you later split the name.
	public String getFirstName() {
		if (this.name != null && this.name.contains(" ")) {
			return this.name.substring(0, this.name.indexOf(" "));
		}
		return this.name; // Or return null/empty string if no space
	}

	public String getLastName() {
		if (this.name != null && this.name.contains(" ")) {
			return this.name.substring(this.name.indexOf(" ") + 1);
		}
		return ""; // Or return null/empty string
	}

}
