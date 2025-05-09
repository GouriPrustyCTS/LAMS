package com.leave.lams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LamsApplication.class, args);
	}

}


/*	API documentation
 * 
 
 /health -> to check health -> app is working or not
 /addCustomer -> to insert customer in DB -> DB is working or not and all proper connection happened or not
  * 
  * {
    "custName": "John",
    "custph": 917651
    }
 */


/*	[POST] -> localhost:8080/employee/add
 {
  "name": "Alice Johnson",
  "department": "IT",
  "hireDate": "2025-06-01",
  "email": "john@gmail.com",
  "jobTitle": "FSE",
  "leaveBalances": [
    {
      "leaveType": "Vacation",
      "balance": 10
    },
    {
      "leaveType": "Sick",
      "balance": 5
    }
  ],
  "leaveRequests": [
    {
      "leaveType": "Vacation",
      "startDate": "2025-06-01",
      "endDate": "2025-06-05",
      "requestDate": "2025-05-07T09:00:00",
      "reason": "Vacation Trip",
      "status": "Approved"
    }
  ],
  "shifts": [
    {
      "shiftDate": "2025-05-10",
      "shiftStartTime": "09:00",
      "shiftEndTime": "17:00"
    }
  ],
  "attendances": [
    {
      "clockInTime": "2025-05-07T09:00:00",
      "clockOutTime": "2025-05-07T17:00:00",
      "workHours": 8,
      "attendanceDate": "2025-05-10"
    }
  ],
  "reports": [
    {
      "dateRangeStart": "2025-05-01",
      "dateRangeEnd": "2025-05-01",
      "generatedDate": "2025-05-07T17:00:00",
      "totalAttendance": 5,
      "absenteesim": 1
    }
  ]
} 


___________[POST] -> /attendance/add

{
    "clockInTime": "2025-05-07T09:00:00",
    "clockOutTime": "2025-05-07T17:00:00",
    "workHours": 8,
    "attendanceDate": "2025-05-10",
    "employee":{
        "employeeId":2
    }
}

__________[POS] -> /leaveBalances/add

{
    "leaveType": "Vacation",
    "balance": 25,
    "employee":{
        "employeeId":2
    }
}
__________[POST] -> /leaveRequests/add
{
      "leaveType": "Marriage",
      "startDate": "2025-06-01",
      "endDate": "2025-06-05",
      "requestDate": "2025-05-07T09:00:00",
      "reason": "Honey moon",
      "status": "Approved",
      "employee":{
        "employeeId":2
      }
}
__________[POST] -> /attendanceReports/add

{
      "dateRangeStart": "2025-05-01",
      "dateRangeEnd": "2025-05-05",
      "generatedDate": "2025-05-07T17:00:00",
      "totalAttendance": 10,
      "absenteesim": 2,
      "employee":{
        "employeeId":2
      }
}
__________[POST] -> /shift/add
{
      "shiftDate": "2025-05-10",
      "shiftStartTime": "09:00",
      "shiftEndTime": "19:00",
      "employee":{
        "employeeId":2
      }
    }
    
    
 */
