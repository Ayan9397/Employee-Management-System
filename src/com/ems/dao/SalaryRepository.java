package com.ems.dao;

import com.ems.model.SalaryRecord;
import java.util.Map;

/**
 * Data Access Object (DAO) interface for Salary and Payroll management.
 * Demonstrates SQL Aggregation and reporting contracts.
 */
public interface SalaryRepository {
    SalaryRecord findByEmployeeId(String employeeId);
    boolean saveOrUpdate(SalaryRecord record);
    
    /**
     * Retrieves aggregated payroll statistics:
     * - Total Monthly Payroll (SUM)
     * - Average Monthly Salary (AVG)
     * - Highest Monthly Salary (MAX)
     * - Lowest Monthly Salary (MIN)
     */
    Map<String, Object> getSalaryStatistics();
}

