package com.ems.model;

import java.time.LocalDate;

/**
 * Abstract base class representing a generic employee in the organization.
 * Demonstrates:
 * 1. Abstraction - declares common state and abstract calculation methods.
 * 2. Encapsulation - private fields with validated getters and setters.
 * 3. Polymorphism - implements SalaryCalculatable with subtype-specific calculation.
 */
public abstract class Employee implements SalaryCalculatable, Identifiable {

    // Encapsulated state (Private fields)
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private int departmentId;
    private String departmentName; // Transient / JOIN field
    private int roleId;
    private String roleTitle;      // Transient / JOIN field

    // Default constructor
    public Employee() {
    }

    // Parameterized constructor
    public Employee(String employeeId, String firstName, String lastName, String email,
                    String phone, LocalDate hireDate, int departmentId, int roleId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.departmentId = departmentId;
        this.roleId = roleId;
    }

    // Abstract methods to be implemented polymorphically by subclasses
    public abstract String getEmploymentType();

    @Override
    public abstract double calculateMonthlySalary();

    // Concrete helper methods
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    @Override
    public String getId() {
        return employeeId;
    }

    // Getters and Setters (Encapsulation)
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }
        this.lastName = lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email.trim().toLowerCase();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone.trim() : "";
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName != null ? departmentName : "Unassigned";
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleTitle() {
        return roleTitle != null ? roleTitle : "Unassigned";
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Dept: %s, Role: %s, Net Monthly: $%,.2f",
                employeeId, getFullName(), getEmploymentType(),
                getDepartmentName(), getRoleTitle(), calculateMonthlySalary());
    }
}

