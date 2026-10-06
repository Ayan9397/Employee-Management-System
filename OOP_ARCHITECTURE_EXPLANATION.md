# Interview Preparation & Architecture Guide: Employee Management System

This document is your **cheat-sheet for technical interviews and recruiter discussions**. It maps every OOP principle, SQL feature, and JDBC concept directly to the code implemented in this project.

---

## Quick Recruiter Q&A

### 1. "Show me where you used OOP."
> **Your Answer:**  
> "In this project, I modeled employees using an abstract base class `Employee` and extended it into concrete classes `FullTimeEmployee` and `ContractEmployee`.  
> - **Encapsulation:** All fields (like `annualSalary`, `hourlyRate`, `email`) are marked `private`. Invariants are enforced inside getters and setters with validation rules.  
> - **Inheritance:** `FullTimeEmployee` and `ContractEmployee` inherit shared state (`employeeId`, `firstName`, `departmentId`, `hireDate`) from `Employee`.  
> - **Polymorphism:** The `calculateMonthlySalary()` method is declared abstract in `Employee` (via the `SalaryCalculatable` interface) and overridden polymorphically in each subclass. When iterating over a `List<Employee>`, calling `emp.calculateMonthlySalary()` executes the subclass calculation at runtime without any `if-else` or `instanceof` switching.  
> - **Abstraction & Interfaces:** Data access is abstracted behind interfaces (`EmployeeRepository`, `DepartmentRepository`, `SalaryRepository`), decoupling business logic in `EmployeeService` from database persistence details."

---

### 2. "Why did you use inheritance instead of just putting all fields in one Employee class?"
> **Your Answer:**  
> "A full-time employee has an annual salary, retirement benefits, and monthly bonuses, whereas a contractor has an hourly rate, hours worked per billing cycle, and a contract duration.  
> If I kept them in a single class, several fields would be nullable or irrelevant for one type (e.g., hourly rate for a full-time employee), violating the **Single Responsibility Principle** and cluttering the domain model. Inheritance allows each class to encapsulate only the state and logic relevant to its specific contract type."

---

### 3. "How did you connect Java with MySQL?"
> **Your Answer:**  
> "I used **JDBC (Java Database Connectivity)** with `mysql-connector-java`:
> 1. Loaded the driver: `Class.forName("com.mysql.cj.jdbc.Driver")`.
> 2. Established connections via `DriverManager.getConnection(url, user, password)`.
> 3. Used **`PreparedStatement`** for all parameterized queries to prevent SQL injection and benefit from query precompilation.
> 4. Managed **ACID Transactions**: For multi-table operations (such as saving an employee and their corresponding salary record), I disabled auto-commit with `conn.setAutoCommit(false)`, executed the queries, invoked `conn.commit()`, and safely executed `conn.rollback()` in the `catch` block if an exception occurred.
> 5. Employed **try-with-resources** blocks to ensure database connections, statements, and result sets are always closed deterministically to avoid connection leaks."

---

### 4. "What SQL did you use in this project?"
> **Your Answer:**  
> "I implemented full relational database operations across 4 normalized tables:
> - **CRUD:** `INSERT`, `UPDATE`, `DELETE`, and `SELECT` queries across `employees`, `departments`, `roles`, and `salary`.
> - **Multi-Table JOINs:** Used `LEFT JOIN` and `INNER JOIN` in `JdbcEmployeeRepositoryImpl` to retrieve employee records along with their department names, role titles, and salary details in a single query.
> - **Filtering & Pattern Matching:** Used `WHERE LOWER(...) LIKE ?` with wildcard binding for fast multi-field searching (by ID, first name, last name, email, department).
> - **SQL Aggregations & GROUP BY:** In `JdbcSalaryRepositoryImpl`, I used `COUNT(*)`, `SUM(net_monthly_salary)`, `AVG(net_monthly_salary)`, `MIN`, `MAX`, and `GROUP BY department_id, department_name` to calculate department-level payroll expenses."

---

## Object-Oriented Architecture Breakdown

```
                 +-----------------------+
                 |  <<interface>>        |
                 |  SalaryCalculatable   |
                 +-----------------------+
                 | +calculateMonthlySalary()
                 +-----------------------+
                             ^
                             | (implements)
                 +-----------------------+
                 |      <<abstract>>     |
                 |        Employee       |
                 +-----------------------+
                 | - employeeId: String  |
                 | - firstName: String   |
                 | - lastName: String    |
                 | - email: String       |
                 | - hireDate: LocalDate |
                 | - departmentId: int   |
                 +-----------------------+
                             ^
             +---------------+---------------+
             |                               |
 +-----------------------+       +-----------------------+
 |   FullTimeEmployee    |       |   ContractEmployee    |
 +-----------------------+       +-----------------------+
 | - annualSalary: double|       | - hourlyRate: double  |
 | - monthlyBonus: double|       | - hoursWorked: double |
 | - benefits: String    |       | - durationMonths: int |
 +-----------------------+       +-----------------------+
 | +calculateMonthlySalary()     | +calculateMonthlySalary()
 +-----------------------+       +-----------------------+
```

### Key OOP Principles in the Codebase

| Principle | Class / File | Demonstration |
|---|---|---|
| **Abstraction** | `Employee.java`, `SalaryCalculatable.java` | Defines the essential contract without revealing internal calculation logic. |
| **Encapsulation** | `Employee.java`, `FullTimeEmployee.java` | Private fields with strict validation in setters (e.g. valid email syntax, positive salaries). |
| **Inheritance** | `FullTimeEmployee.java`, `ContractEmployee.java` | Code reuse of core demographics and department relationships. |
| **Polymorphism** | `TablePrinter.java`, `JdbcEmployeeRepositoryImpl.java` | Polymorphic collection traversal and dynamic object mapping from SQL ResultSets. |
| **Interface Segregation** | `EmployeeRepository.java`, `DepartmentRepository.java` | Clean, distinct contracts for different domain entities. |
| **Factory Pattern** | `DaoFactory.java` | Decouples caller code from the concrete persistence mechanism (JDBC vs In-Memory). |

---

## Relational Database Schema & Relationships

```
+--------------------+        1:N        +--------------------+
|    departments     | ----------------< |     employees      |
+--------------------+                   +--------------------+
| department_id (PK) |                   | employee_id (PK)   |
| department_code    |                   | first_name         |
| department_name    |                   | last_name          |
| location           |                   | email (UNIQUE)     |
| budget             |                   | department_id (FK) |
+--------------------+                   | role_id (FK)       |
                                         | employment_type    |
+--------------------+        1:N        +--------------------+
|       roles        | ----------------<           |
+--------------------+                             | 1:1
| role_id (PK)       |                             v
| role_title         |                   +--------------------+
| description        |                   |       salary       |
+--------------------+                   +--------------------+
                                         | salary_id (PK)     |
                                         | employee_id (FK)   |
                                         | base_or_annual_sal |
                                         | hourly_rate        |
                                         | hours_worked       |
                                         | net_monthly_salary |
                                         +--------------------+
```
