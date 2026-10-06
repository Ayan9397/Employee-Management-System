-- ========================================================
-- Employee Management System (EMS) Database Schema
-- Stack: Java + OOP + JDBC + MySQL
-- ========================================================

CREATE DATABASE IF NOT EXISTS employee_management_db;
USE employee_management_db;

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(20) NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    budget DECIMAL(15,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Roles Table
CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_title VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Employees Table
CREATE TABLE IF NOT EXISTS employees (
    employee_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    hire_date DATE NOT NULL,
    department_id INT,
    role_id INT,
    employment_type ENUM('FULL_TIME', 'CONTRACT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL
);

-- 4. Salary Table
CREATE TABLE IF NOT EXISTS salary (
    salary_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    base_or_annual_salary DECIMAL(12, 2) DEFAULT 0.00,
    hourly_rate DECIMAL(10, 2) DEFAULT 0.00,
    hours_worked DECIMAL(6, 2) DEFAULT 0.00,
    bonus DECIMAL(10, 2) DEFAULT 0.00,
    deductions DECIMAL(10, 2) DEFAULT 0.00,
    net_monthly_salary DECIMAL(12, 2) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- ========================================================
-- Sample Seed Data
-- ========================================================

-- Seed Departments
INSERT INTO departments (department_code, department_name, location, budget) VALUES
('DEPT-ENG', 'Engineering', 'Building A - Floor 4', 750000.00),
('DEPT-HR', 'Human Resources', 'Building B - Floor 2', 200000.00),
('DEPT-FIN', 'Finance', 'Building A - Floor 2', 350000.00),
('DEPT-MKT', 'Marketing', 'Building C - Floor 1', 300000.00)
ON DUPLICATE KEY UPDATE department_name=VALUES(department_name);

-- Seed Roles
INSERT INTO roles (role_title, description) VALUES
('Software Engineer', 'Develops and maintains core backend and frontend systems'),
('Lead Architect', 'Designs enterprise architecture and mentors developers'),
('HR Specialist', 'Handles recruitment, onboarding, and employee relations'),
('Financial Analyst', 'Monitors company expenses, financial planning, and budgets'),
('Product Designer', 'Creates user experience specifications and UI mockups')
ON DUPLICATE KEY UPDATE role_title=VALUES(role_title);

-- Seed Employees
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, department_id, role_id, employment_type) VALUES
('EMP-FT-1001', 'Ayan', 'Khan', 'ayan.khan@company.com', '+91-9876543210', '2024-01-15', 1, 1, 'FULL_TIME'),
('EMP-FT-1002', 'Sarah', 'Jenkins', 'sarah.j@company.com', '+91-9812345678', '2023-08-10', 2, 3, 'FULL_TIME'),
('EMP-CT-2001', 'Rahul', 'Verma', 'rahul.v@contractor.com', '+91-9723456789', '2024-05-01', 1, 2, 'CONTRACT'),
('EMP-CT-2002', 'Emily', 'Chen', 'emily.c@contractor.com', '+91-9654321876', '2024-06-12', 4, 5, 'CONTRACT')
ON DUPLICATE KEY UPDATE first_name=VALUES(first_name);

-- Seed Salaries
-- For FullTime: base_or_annual_salary = Annual salary, net_monthly_salary = (annual / 12) + bonus - deductions
-- For Contract: hourly_rate * hours_worked - deductions
INSERT INTO salary (employee_id, base_or_annual_salary, hourly_rate, hours_worked, bonus, deductions, net_monthly_salary) VALUES
('EMP-FT-1001', 960000.00, 0.00, 0.00, 5000.00, 2500.00, 82500.00),
('EMP-FT-1002', 600000.00, 0.00, 0.00, 3000.00, 1500.00, 51500.00),
('EMP-CT-2001', 0.00, 1200.00, 160.00, 0.00, 5000.00, 187000.00),
('EMP-CT-2002', 0.00, 950.00, 140.00, 0.00, 3000.00, 130000.00)
ON DUPLICATE KEY UPDATE net_monthly_salary=VALUES(net_monthly_salary);

