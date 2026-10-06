package com.ems.service;

import com.ems.dao.DaoFactory;
import com.ems.dao.DepartmentRepository;
import com.ems.model.Department;
import com.ems.util.ValidationUtil;

import java.util.List;

/**
 * Service Layer for managing Departments.
 */
public class DepartmentService {

    private final DepartmentRepository departmentRepo;

    public DepartmentService() {
        this.departmentRepo = DaoFactory.getDepartmentRepository();
    }

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartmentById(int id) {
        return departmentRepo.findById(id);
    }

    public boolean createDepartment(String code, String name, String location, double budget) {
        if (!ValidationUtil.isNotEmpty(code) || !ValidationUtil.isNotEmpty(name)) {
            throw new IllegalArgumentException("Department code and name cannot be empty.");
        }
        if (!ValidationUtil.isPositive(budget)) {
            throw new IllegalArgumentException("Department budget cannot be negative.");
        }
        Department dept = new Department(0, code.trim().toUpperCase(), name.trim(), location.trim(), budget);
        return departmentRepo.save(dept);
    }
}

