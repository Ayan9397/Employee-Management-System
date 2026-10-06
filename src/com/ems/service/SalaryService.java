package com.ems.service;

import com.ems.dao.DaoFactory;
import com.ems.dao.SalaryRepository;
import com.ems.model.SalaryRecord;

import java.util.Map;

/**
 * Service Layer for Salary & Payroll operations.
 */
public class SalaryService {

    private final SalaryRepository salaryRepo;

    public SalaryService() {
        this.salaryRepo = DaoFactory.getSalaryRepository();
    }

    public SalaryRecord getSalaryForEmployee(String employeeId) {
        return salaryRepo.findByEmployeeId(employeeId);
    }

    public Map<String, Object> getPayrollStatistics() {
        return salaryRepo.getSalaryStatistics();
    }
}

