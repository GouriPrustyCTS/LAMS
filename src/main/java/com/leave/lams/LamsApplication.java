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
 AUTH
 
 To login as Admin
 [POST] -> localhost:8080/login?user=admin
 x-www-url :
 username : root@gmail.com
 password : root
 
 To login as Employee
 [POST] -> localhost:8080/login?user=employee
  x-www-url :
 username : devans@gmail.com
 password : user
 
 
 to logout
 [POST] -> localhost:8080/logout
 
 
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
  "name": "Karthick",
  "department": "CSE",
  "hireDate": "2025-03-17",
  "email": "karti@gmail.com",
  "jobTitle": "White Hacker"
 }

___________[POST] -> /attendance/add

{
    "clockInTime": "2025-05-07T09:00:00",
    "clockOutTime": "2025-05-07T17:00:00",
    "workHours": 8,
    "attendanceDate": "2025-05-10",
    "employeeId":4
}

__________[POS] -> /leaveBalances/add

{
    "leaveType": "College Exams",
    "balance": 10,
    "employeeId":2
}
__________[POST] -> /leaveRequests/add
{
      "leaveType": "College Exams",	//leaveType value should be present
      "startDate": "2025-06-01",
      "endDate": "2025-06-02",
      "requestDate": "2025-05-07T09:00:00",
      "reason": "Project work",
      "status": "PENDING",
      "employeeId":1
}
__________[POST] -> /attendanceReports/add

{
      "dateRangeStart": "2025-05-01",
      "dateRangeEnd": "2025-05-05",
      "generatedDate": "2025-05-07T17:00:00",
      "totalAttendance": 1,
      "absenteesim": 0,
      "employeeId":5
}
__________[POST] -> /shift/add
{
      "shiftDate": "2025-05-10",
      "shiftStartTime": "09:00",
      "shiftEndTime": "19:00",
        "employeeId":2
}
    
    
    _______[POST] -> /swap/request
{
    "fromEmployeeId": 3,
    "toEmployeeId": 5,
    "fromShiftId": 2,
    "toShiftId": 4,
    "status": "PENDING"
}



__________________[GET] -> charts

../reports/tru-time-bar-chart?empId=1

 */
