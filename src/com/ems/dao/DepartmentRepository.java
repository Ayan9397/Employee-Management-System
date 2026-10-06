package com.ems.dao;

import com.ems.model.Department;
import java.util.List;

/**
 * Data Access Object (DAO) interface for Department management.
 */
public interface DepartmentRepository {
    List<Department> findAll();
    Department findById(int departmentId);
    boolean save(Department department);
    int getEmployeeCountByDepartment(int departmentId);
}

