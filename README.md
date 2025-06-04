
# 🚀 Employee Leave and Attendance Management System (LAMS)

LAMS is a comprehensive solution designed to **automate and streamline** employee attendance tracking, leave request management, and working hour calculations. It provides a robust platform for both employees and managers, ensuring efficient workforce management and accurate record-keeping.

---

## 🧩 Key Modules

### 👥 Employee Attendance
- Track daily **clock-in/out** times
- View **attendance history**
- Monitor **irregularities**

### 📝 Leave Management
- Request various leave types (e.g., **sick**, **vacation**)
- Track **leave request statuses**
- **Manager approval** workflows

### 📊 Leave Balance
- Automatically track and update **leave balances**
- Reflects **approved/rejected** leaves
- Adheres to **company policies**

### 🕒 Shift Management
- Assign shifts to employees
- View and request **shift swaps**
- **Manager approval** for changes

### 📈 Reports & Analytics
- Generate reports on:
  - **Attendance trends**
  - **Leave usage**
  - **Shift coverage**
- Aid in **management decisions**

---

## 🛠️ Technology Stack

### Frontend
- **Angular** (Primary)

### Backend
- **Spring Boot (Java)**
- REST API-based architecture

### Database
- **MySQL** (configured via `application.properties`)

---

## 🏗️ Architecture

LAMS follows a **client-server architecture** with a clear separation of concerns:

- **Frontend**: Communicates with the backend via **REST APIs**
- **Backend**: Handles business logic, data processing, and database interactions

---

## ✨ Features

### 👤 Employee Module
- **Clock In/Out**: Mark daily attendance
- **Attendance History**: View records and working hours
- **Leave Requests**: Submit various leave types
- **Shift View**: See assigned shifts and request swaps

### 👨‍💼 Manager Module
- **Attendance Monitoring**: Real-time dashboard
- **Leave Approval**: Approve or reject leave requests
- **Shift Assignment**: Create and assign shifts
- **Reports**: Generate detailed analytics

