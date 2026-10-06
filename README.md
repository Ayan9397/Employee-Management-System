# Employee Management System (EMS)

> ### 🌐 Live Interactive Web Demo
> - **Interactive Browser Demo**: **[https://ayan9397.github.io/Employee-Management-System/](https://ayan9397.github.io/Employee-Management-System/)**
> - **GitHub Repository**: **[https://github.com/Ayan9397/Employee-Management-System](https://github.com/Ayan9397/Employee-Management-System)**
> - **Architecture & Interview Guide**: **[OOP Architecture Explanation](https://github.com/Ayan9397/Employee-Management-System/blob/main/OOP_ARCHITECTURE_EXPLANATION.md)**

[![Live Demo](https://img.shields.io/badge/Live_Demo-ayan9397.github.io%2FEmployee--Management--System-success.svg?style=for-the-badge&logo=githubpages)](https://ayan9397.github.io/Employee-Management-System/)
[![Java](https://img.shields.io/badge/Java-21%20%7C%2023-orange.svg?style=for-the-badge&logo=java)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

A comprehensive, production-grade **Employee Management System** built with **Java (OOP)**, **JDBC**, and **MySQL**. Designed to demonstrate software engineering best practices, design patterns, clean architecture, and technical interview readiness.

---

## 🚀 Features

- ✅ **Add Employee**: Multi-type employee onboarding (`Full-Time` and `Contractor`) with dynamic monthly salary calculation.
- ✅ **Update Employee**: Modify demographics, department, role, annual salary, or hourly rate with immediate recalculation.
- ✅ **Delete Employee**: Relational cascade deletion to salary records.
- ✅ **Search Employee**: Multi-field pattern matching across Employee ID, First/Last Name, Email, and Department.
- ✅ **View All Employees**: Aligned ASCII tabular report displaying employee IDs, types, roles, departments, and net monthly compensations.
- ✅ **Department Management**: View departments, track allocated budgets, create new departments, and inspect employees grouped by department.
- ✅ **Salary & Payroll Analytics**: Real-time aggregation showing Total Monthly Payroll expense (`SUM`), Average Salary (`AVG`), Min/Max Take-Home, and Department-wise breakdown (`GROUP BY`).
- ✅ **Automatic Employee ID Generation**: Formatted identifiers (`EMP-FT-100X` for Full-Time, `EMP-CT-200X` for Contractors).
- ✅ **Input Validation**: Robust regex validation for email and phone numbers, defensive bounds checking for non-negative salaries and numbers.
- ✅ **Dual-Mode Persistence**:
  - **Live MySQL (JDBC)**: Full ACID transactions, PreparedStatements, JOIN queries, and aggregations.
  - **In-Memory Demo Mode**: Zero-configuration fallback pre-seeded with realistic data when demonstrating offline or without MySQL credentials.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Java 23 (compatible with Java 8+)
- **Database:** MySQL 8.0
- **Driver:** MySQL Connector/J 8.0.26 (included in `lib/`)
- **Architecture:** Layered Architecture (`Model` -> `DAO / Repository` -> `Service` -> `UI / CLI`)

### Folder Structure
```
Engineers Project/
└── Employee Management System/
    ├── bin/                              # Compiled .class files
    ├── lib/
    │   └── mysql-connector-java.jar      # JDBC MySQL driver (included)
    ├── sql/
    │   └── schema.sql                    # MySQL tables, relationships & seed data
    ├── src/
    │   └── com/ems/
    │       ├── Main.java                 # Entry point
    │       ├── model/                    # OOP Entities (Employee, FullTime, Contract, etc.)
    │       ├── dao/                      # DAO interfaces & Factory
    │       │   ├── jdbc/                 # JDBC implementation (SQL queries & transactions)
    │       │   └── memory/               # In-memory implementation (demo fallback)
    │       ├── service/                  # Business logic & validation orchestration
    │       ├── ui/                       # Interactive CLI menu & views
    │       └── util/                     # Database connection, validation & table formatting
    ├── db.properties                     # Database connection configuration
    ├── compile.bat                       # Script to compile Java source files
    ├── run.bat                           # Script to compile & run the application
    ├── setup_mysql.bat                   # Script to import schema.sql into MySQL
    ├── OOP_ARCHITECTURE_EXPLANATION.md   # Recruiter Q&A & Interview cheat-sheet
    └── README.md                         # Project documentation
```

---

## ⚡ Quick Start

### 1. Run Directly (Demo Mode / Live Mode)
Double-click `run.bat` or run in PowerShell / Command Prompt:
```powershell
cd "C:\Users\mohda\OneDrive\Desktop\Engineers Project\Employee Management System"
.\run.bat
```
*(The system automatically tests the connection: if MySQL credentials are ready, it connects via live JDBC; otherwise, it safely runs in In-Memory Demo Mode with pre-seeded data so you can test all features immediately).*

### 2. Connect to Live MySQL
1. Ensure MySQL Server is running.
2. Run `setup_mysql.bat` (or import `sql/schema.sql` into MySQL Workbench / CLI):
   ```powershell
   .\setup_mysql.bat
   ```
3. Update your credentials in `db.properties` (or use Option 8 in the console menu to enter your MySQL username/password interactively).

---

## 📚 Interview Preparation Guide
See [OOP_ARCHITECTURE_EXPLANATION.md](file:///C:/Users/mohda/OneDrive/Desktop/Engineers%20Project/Employee%20Management%20System/OOP_ARCHITECTURE_EXPLANATION.md) for clear explanations and talking points for recruiter questions regarding OOP, JDBC, and SQL design.
