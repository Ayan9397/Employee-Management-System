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

## 📚 Technical Competency Proof (CISIN & Technical Interview Mapping)

| Core Requirement | Implementation Evidence in EMS | Key Classes & Methods |
|---|---|---|
| **Classes & Objects** | Real-world business entities with constructor overloading and state management | `Employee.java`, `FullTimeEmployee.java`, `ContractEmployee.java`, `Department.java` |
| **Inheritance** | Base abstraction extended by specialized employee models reusing common fields | `FullTimeEmployee extends Employee`<br>`ContractEmployee extends Employee` |
| **Polymorphism** | Dynamic method dispatch where compensation calculation rules differ per employee type | `@Override public double calculateMonthlySalary()` implemented differently for Full-Time vs Contractors |
| **Encapsulation** | Strict private field visibility, defensive bounds validation, and immutable identifiers | Getters/setters with regex bounds checking (`ValidationUtil.isValidEmail()`, `salary >= 0`) |
| **Abstraction & Interfaces** | Contract separation decoupling callers from persistent storage and business contracts | `SalaryCalculatable.java`, `Identifiable.java`, `EmployeeRepository.java` (DAO) |
| **Exception Handling** | Granular SQL error interception, transaction rollbacks, and defensive CLI input guards | `try-catch (SQLException e)` with `conn.rollback()` in `JdbcSalaryRepository.java` |
| **Collections Framework** | Dynamic in-memory manipulation, department groupings, and aggregate sorting | `List<Employee>`, `ArrayList`, `Map<String, Double>` for payroll aggregations |
| **JDBC & MySQL** | Direct relational connectivity, parameter binding, and prepared statement caching | `PreparedStatement`, `ResultSet`, `DriverManager`, `DatabaseConnection.java` |
| **SQL & ACID Transactions** | Relational 3NF tables, foreign key cascades, and manual commit boundary management | `conn.setAutoCommit(false)`, `conn.commit()`, `conn.rollback()` |

---

## 🏛️ Object-Oriented Architecture Deep-Dive

### 1. Polymorphism & Interface Abstraction
```java
// Common contract across all payable personnel
public interface SalaryCalculatable {
    double calculateMonthlySalary();
}

// Concrete Full-Time Implementation: Base salary / 12 + annual bonus
public class FullTimeEmployee extends Employee {
    private double annualSalary;
    private double bonusPercentage;

    @Override
    public double calculateMonthlySalary() {
        return (annualSalary / 12.0) + (annualSalary * bonusPercentage / 12.0);
    }
}

// Concrete Contractor Implementation: Hourly billing rate * hours worked
public class ContractEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    @Override
    public double calculateMonthlySalary() {
        return hourlyRate * (double) hoursWorked;
    }
}
```

### 2. JDBC & SQL Injection Prevention (`PreparedStatement`)
All SQL interactions in `JdbcEmployeeRepository` and `JdbcSalaryRepository` use parameterized queries:
```java
String sql = "INSERT INTO employees (employee_id, first_name, last_name, email, department_id, employee_type) " +
             "VALUES (?, ?, ?, ?, ?, ?)";
try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    stmt.setString(1, emp.getId());
    stmt.setString(2, emp.getFirstName());
    stmt.setString(3, emp.getLastName());
    stmt.setString(4, emp.getEmail());
    stmt.setInt(5, emp.getDepartmentId());
    stmt.setString(6, emp.getEmployeeType());
    stmt.executeUpdate();
}
```

### 3. ACID Transactions with Rollback
When creating or modifying employees and associated salary records, operations run inside an atomic transaction:
```java
try {
    conn.setAutoCommit(false);
    employeeDao.save(conn, employee);
    salaryDao.recordSalary(conn, salaryRecord);
    conn.commit(); // Atomic commit
} catch (SQLException e) {
    conn.rollback(); // Safe rollback on failure
    throw new DatabaseOperationException("Transaction failed: " + e.getMessage(), e);
} finally {
    conn.setAutoCommit(true);
}
```

---

## ⚡ Quick Start

### 1. Run Live in Browser (Zero Install)
Launch the interactive web demo directly at **[https://ayan9397.github.io/Employee-Management-System/](https://ayan9397.github.io/Employee-Management-System/)**.

### 2. Run Locally via Command Line (CLI)
Double-click `run.bat` or run in PowerShell / Command Prompt:
```powershell
cd "C:\Users\mohda\OneDrive\Desktop\Engineers Project\Employee Management System"
.\run.bat
```
*(The system automatically tests the connection: if MySQL credentials are ready, it connects via live JDBC; otherwise, it safely runs in In-Memory Demo Mode with pre-seeded data so you can test all features immediately).*

### 3. Connect to Live MySQL
1. Ensure MySQL Server is running.
2. Run `setup_mysql.bat` (or import `sql/schema.sql` into MySQL Workbench / CLI):
   ```powershell
   .\setup_mysql.bat
   ```
3. Update credentials in `db.properties` or toggle mode in Menu Option 8.

---

## 📚 Interview Preparation Guide
See [OOP_ARCHITECTURE_EXPLANATION.md](file:///C:/Users/mohda/OneDrive/Desktop/Engineers%20Project/Employee%20Management%20System/OOP_ARCHITECTURE_EXPLANATION.md) for clear explanations and talking points for technical interview questions regarding OOP, JDBC, and SQL design.
