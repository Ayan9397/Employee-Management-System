package com.ems.dao;

import com.ems.model.Employee;
import java.util.List;

/**
 * Data Access Object (DAO) interface for Employee entities.
 * Demonstrates Abstraction by separating data access specification from implementation.
 */
public interface EmployeeRepository {
    boolean save(Employee employee);
    boolean update(Employee employee);
    boolean delete(String employeeId);
    Employee findById(String employeeId);
    List<Employee> findAll();
    List<Employee> search(String keyword);
    List<Employee> findByDepartment(int departmentId);
    int countTotalEmployees();
    String generateNextEmployeeId(String employmentType);
}

