package com.ems.model;

/**
 * Interface representing any entity capable of monthly salary calculation.
 * Demonstrates Abstraction & Interface-based design.
 */
public interface SalaryCalculatable {
    /**
     * Calculates the net monthly take-home salary.
     * Concrete calculation varies polymorphically based on employment type.
     *
     * @return net monthly salary
     */
    double calculateMonthlySalary();
}

