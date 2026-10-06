package com.ems.model;

import java.time.LocalDateTime;

/**
 * Model representing the persisted salary snapshot for an employee in the database.
 */
public class SalaryRecord {
    private int salaryId;
    private String employeeId;
    private double baseOrAnnualSalary;
    private double hourlyRate;
    private double hoursWorked;
    private double bonus;
    private double deductions;
    private double netMonthlySalary;
    private LocalDateTime lastUpdated;

    public SalaryRecord() {
    }

    public SalaryRecord(int salaryId, String employeeId, double baseOrAnnualSalary,
                        double hourlyRate, double hoursWorked, double bonus,
                        double deductions, double netMonthlySalary, LocalDateTime lastUpdated) {
        this.salaryId = salaryId;
        this.employeeId = employeeId;
        this.baseOrAnnualSalary = baseOrAnnualSalary;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.bonus = bonus;
        this.deductions = deductions;
        this.netMonthlySalary = netMonthlySalary;
        this.lastUpdated = lastUpdated;
    }

    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getBaseOrAnnualSalary() {
        return baseOrAnnualSalary;
    }

    public void setBaseOrAnnualSalary(double baseOrAnnualSalary) {
        this.baseOrAnnualSalary = baseOrAnnualSalary;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNetMonthlySalary() {
        return netMonthlySalary;
    }

    public void setNetMonthlySalary(double netMonthlySalary) {
        this.netMonthlySalary = netMonthlySalary;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

