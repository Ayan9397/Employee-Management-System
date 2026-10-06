package com.ems.model;

import java.time.LocalDate;

/**
 * Represents a permanent, full-time salaried employee.
 * Demonstrates:
 * 1. Inheritance - extends Employee base class.
 * 2. Polymorphism - overrides calculateMonthlySalary() based on annual salary and benefits.
 */
public class FullTimeEmployee extends Employee {

    private double annualSalary;
    private double monthlyBonus;
    private double monthlyDeductions;
    private String benefitsPackage;

    public FullTimeEmployee() {
        super();
    }

    public FullTimeEmployee(String employeeId, String firstName, String lastName, String email,
                            String phone, LocalDate hireDate, int departmentId, int roleId,
                            double annualSalary, double monthlyBonus, double monthlyDeductions,
                            String benefitsPackage) {
        super(employeeId, firstName, lastName, email, phone, hireDate, departmentId, roleId);
        setAnnualSalary(annualSalary);
        setMonthlyBonus(monthlyBonus);
        setMonthlyDeductions(monthlyDeductions);
        this.benefitsPackage = benefitsPackage != null ? benefitsPackage : "Standard Health & 401(k)";
    }

    @Override
    public String getEmploymentType() {
        return "FULL_TIME";
    }

    /**
     * Polymorphic Salary Calculation for Full-Time:
     * Monthly Base = Annual Salary / 12
     * Net Monthly = Monthly Base + Bonus - Deductions
     */
    @Override
    public double calculateMonthlySalary() {
        double monthlyBase = annualSalary / 12.0;
        double net = monthlyBase + monthlyBonus - monthlyDeductions;
        return Math.max(0.0, net);
    }

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        if (annualSalary < 0) {
            throw new IllegalArgumentException("Annual salary cannot be negative.");
        }
        this.annualSalary = annualSalary;
    }

    public double getMonthlyBonus() {
        return monthlyBonus;
    }

    public void setMonthlyBonus(double monthlyBonus) {
        if (monthlyBonus < 0) {
            throw new IllegalArgumentException("Bonus cannot be negative.");
        }
        this.monthlyBonus = monthlyBonus;
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

    public String getBenefitsPackage() {
        return benefitsPackage;
    }

    public void setBenefitsPackage(String benefitsPackage) {
        this.benefitsPackage = benefitsPackage;
    }
}

