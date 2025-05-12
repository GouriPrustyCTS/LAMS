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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "leave_request")
public class LeaveRequest {

	private static final Logger logger = LoggerFactory.getLogger(LeaveRequest.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leaveRequestId;

	private Date startDate;
	private Date endDate;
	private LocalDateTime requestDate;
	private String reason;
	private String leaveType;
	private String status;

	@ManyToOne
	@JoinColumn(name = "employeeId", nullable = false)
	@JsonIncludeProperties({"employeeId"})
	private Employee employee;

	// Example of a log statement in a setter
	public void setStatus(String status) {
		logger.debug("Setting leave request status to: {}", status);
		this.status = status;
	}

	public Long getId() {
		return leaveRequestId;
	}
}
