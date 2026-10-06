package com.ems.dao;

import com.ems.dao.jdbc.JdbcDepartmentRepositoryImpl;
import com.ems.dao.jdbc.JdbcEmployeeRepositoryImpl;
import com.ems.dao.jdbc.JdbcRoleRepositoryImpl;
import com.ems.dao.jdbc.JdbcSalaryRepositoryImpl;
import com.ems.dao.memory.InMemoryDepartmentRepositoryImpl;
import com.ems.dao.memory.InMemoryEmployeeRepositoryImpl;
import com.ems.dao.memory.InMemoryRoleRepositoryImpl;
import com.ems.dao.memory.InMemorySalaryRepositoryImpl;
import com.ems.util.DatabaseConnection;

/**
 * Factory class responsible for instantiating DAOs.
 * Demonstrates Factory Pattern and graceful fallback between JDBC MySQL and In-Memory storage.
 */
public class DaoFactory {

    private static boolean useJdbc = false;

    private static EmployeeRepository employeeRepo;
    private static DepartmentRepository departmentRepo;
    private static RoleRepository roleRepo;
    private static SalaryRepository salaryRepo;

    static {
        // Automatically check if MySQL is accessible
        if (DatabaseConnection.testConnection()) {
            DatabaseConnection.initializeDatabase();
            enableJdbc();
        } else {
            enableInMemory();
        }
    }

    public static void enableJdbc() {
        useJdbc = true;
        employeeRepo = new JdbcEmployeeRepositoryImpl();
        departmentRepo = new JdbcDepartmentRepositoryImpl();
        roleRepo = new JdbcRoleRepositoryImpl();
        salaryRepo = new JdbcSalaryRepositoryImpl();
    }

    public static void enableInMemory() {
        useJdbc = false;
        employeeRepo = new InMemoryEmployeeRepositoryImpl();
        departmentRepo = new InMemoryDepartmentRepositoryImpl();
        roleRepo = new InMemoryRoleRepositoryImpl();
        salaryRepo = new InMemorySalaryRepositoryImpl();
    }

    public static boolean isUsingJdbc() {
        return useJdbc;
    }

    public static EmployeeRepository getEmployeeRepository() {
        return employeeRepo;
    }

    public static DepartmentRepository getDepartmentRepository() {
        return departmentRepo;
    }

    public static RoleRepository getRoleRepository() {
        return roleRepo;
    }

    public static SalaryRepository getSalaryRepository() {
        return salaryRepo;
    }
}

