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
@Table(name = "shift_swap_request")
public class ShiftSwapRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_employee_id", nullable = false)
    @JsonIncludeProperties({"employeeId"})
    private Employee fromEmployee;

    @ManyToOne
    @JoinColumn(name = "to_employee_id", nullable = false)
    @JsonIncludeProperties({"employeeId"})
    private Employee toEmployee;

    @ManyToOne
    @JoinColumn(name = "from_shift_id", nullable = false)
    private Shift fromShift;

    @ManyToOne
    @JoinColumn(name = "to_shift_id", nullable = false)
    private Shift toShift;

    private String status; // PENDING, APPROVED, REJECTED
}

/*
 
 Here's the normal workflow for the Shift Swap Request feature, step-by-step, using your existing code structure:

1. Employee Requests a Shift Swap
Frontend / Postman:
Employee sends a POST request to /swap/request with the following JSON:
{
  "fromEmployee": { "employeeId": 1 },
  "toEmployee": { "employeeId": 2 },
  "fromShift": { "shiftId": 101 },
  "toShift": { "shiftId": 102 }
}

Backend (Controller):


This hits:
 ShiftSwapRequestController.createSwapRequest(@RequestBody ShiftSwapRequest request)
DAO Layer:


Sets default status to "PENDING" and saves the request.
DB Outcome:


A new row is created in shift_swap_request table with status = PENDING.

2. Manager Views All or Pending Requests
Manager API Calls:


GET /swap/ → to see all requests
GET /swap/pending → to see only PENDING ones
Backend (Controller):


Hits the respective controller method, calls service -> DAO -> repository to fetch data.
Response:


Returns list of swap requests with employee and shift details.

3. Manager Approves or Rejects Swap
Manager sends a PUT request to /swap/{id}/status with status param:

 URL Example:
 PUT /swap/5/status?status=APPROVED
 or
 PUT /swap/5/status?status=REJECTED


Backend Flow:


Controller → updateStatus()
DAO checks if request exists
If status is APPROVED:
Swaps employees between the two shifts in the DB
Updates status to APPROVED
If status is REJECTED:
Only updates the status to REJECTED, no swap happens.

4. Final Outcome
If approved:


Shift 101’s employee changes from Employee A to Employee B
Shift 102’s employee changes from Employee B to Employee A
If rejected:


Nothing changes except status becomes REJECTED

Summary of Methods Used
Step
Endpoint/Method
Who Uses It
Send swap request
POST /swap/request → createSwapRequest()
Employee
View all requests
GET /swap/ → getAllRequests()
Manager/Admin
View pending requests
GET /swap/pending → getPendingRequests()
Manager/Admin
Approve/Reject swap
PUT /swap/{id}/status?status=APPROVED
Manager


Let me know if you want to add email notifications or restrict roles (e.g., only managers can approve).



 
 * */
