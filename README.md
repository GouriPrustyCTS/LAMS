Employee Leave and Attendance Management System (LAMS)

🚀 Project Overview
The Employee Leave and Attendance Management System (LAMS) is a comprehensive solution designed to automate and streamline employee attendance tracking, leave request management, and working hour calculations. This system aims to provide a robust platform for both employees and managers, ensuring efficient workforce management and accurate record-keeping.

Key Modules
Employee Attendance: For tracking daily clock-in/out times, viewing attendance history, and monitoring irregularities.
Leave Management: Facilitates requesting various leave types (sick, vacation, etc.) and tracking request statuses, with manager approval workflows.
Leave Balance: Automatically tracks and updates leave balances based on company policies and approved/rejected leaves.
Shift Management: Enables assignment of shifts to employees, along with options for viewing and swapping shifts with manager approval.
Reports & Analytics: Generates insightful reports on attendance trends, leave usage, and shift coverage to aid management decisions.
🛠️ Technology Stack
LAMS is built with a modern, scalable architecture designed for flexibility and performance.

Frontend:
Angular (Primary) 
Backend:
Spring Boot (Java)- Current implementation focuses on Spring Boot for Java.
REST API-based architecture for handling business logic and data.
Database:
Relational Database (MySQL) - Configured via application.properties.

🏗️ Architecture
The system follows a client-server architecture with a clear separation of concerns:
Frontend: Communicates with the backend exclusively via REST APIs.
Backend: Handles all business logic, data processing, and interacts with the database for storage and retrieval.

✨ Features
Employee Module
Clock In/Out: Employees can mark their daily attendance.
Attendance History: View personal attendance records and working hours.
Leave Requests: Submit various types of leave requests.
Shift View: See assigned shifts and request swaps.
Manager Module
Attendance Monitoring: Dashboard to track real-time attendance and identify irregularities.
Leave Approval: System for approving or rejecting employee leave requests.
Shift Assignment: Create and assign shifts to employees.
Reports: Generate detailed reports on attendance, leave usage, and shift coverage.
