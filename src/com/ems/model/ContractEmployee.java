package com.ems.model;

import java.time.LocalDate;

/**
 * Represents a contract-based hourly employee.
 * Demonstrates:
 * 1. Inheritance - extends Employee base class.
 * 2. Polymorphism - overrides calculateMonthlySalary() based on hourly rate & hours worked.
 */
public class ContractEmployee extends Employee {

    private double hourlyRate;
    private double hoursWorked;
    private int contractDurationMonths;
    private double monthlyDeductions;

    public ContractEmployee() {
        super();
    }

    public ContractEmployee(String employeeId, String firstName, String lastName, String email,
                            String phone, LocalDate hireDate, int departmentId, int roleId,
                            double hourlyRate, double hoursWorked, int contractDurationMonths,
                            double monthlyDeductions) {
        super(employeeId, firstName, lastName, email, phone, hireDate, departmentId, roleId);
        setHourlyRate(hourlyRate);
        setHoursWorked(hoursWorked);
        setContractDurationMonths(contractDurationMonths);
        setMonthlyDeductions(monthlyDeductions);
    }

    @Override
    public String getEmploymentType() {
        return "CONTRACT";
    }

    /**
     * Polymorphic Salary Calculation for Contract:
     * Gross = Hourly Rate * Hours Worked
     * Net Monthly = Gross - Deductions
     */
    @Override
    public double calculateMonthlySalary() {
        double gross = hourlyRate * hoursWorked;
        double net = gross - monthlyDeductions;
        return Math.max(0.0, net);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        if (hoursWorked < 0) {
            throw new IllegalArgumentException("Hours worked cannot be negative.");
        }
        this.hoursWorked = hoursWorked;
    }

    public int getContractDurationMonths() {
        return contractDurationMonths;
    }

    public void setContractDurationMonths(int contractDurationMonths) {
        if (contractDurationMonths <= 0) {
            this.contractDurationMonths = 6; // Default 6 months
        } else {
            this.contractDurationMonths = contractDurationMonths;
        }
    }

    public double getMonthlyDeductions() {
        return monthlyDeductions;
    }

    public void setMonthlyDeductions(double monthlyDeductions) {
        if (monthlyDeductions < 0) {
            throw new IllegalArgumentException("Deductions cannot be negative.");
        }
        this.monthlyDeductions = monthlyDeductions;
    }
}

