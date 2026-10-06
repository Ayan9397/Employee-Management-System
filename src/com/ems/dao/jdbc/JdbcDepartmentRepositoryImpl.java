package com.ems.dao.jdbc;

import com.ems.dao.DepartmentRepository;
import com.ems.model.Department;
import com.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of DepartmentRepository.
 */
public class JdbcDepartmentRepositoryImpl implements DepartmentRepository {

    @Override
    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM departments ORDER BY department_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_code"),
                        rs.getString("department_name"),
                        rs.getString("location"),
                        rs.getDouble("budget")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch departments: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Department findById(int departmentId) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("department_id"),
                            rs.getString("department_code"),
                            rs.getString("department_name"),
                            rs.getString("location"),
                            rs.getDouble("budget")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch department: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(Department dept) {
        String sql = "INSERT INTO departments (department_code, department_name, location, budget) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dept.getDepartmentCode());
            stmt.setString(2, dept.getDepartmentName());
            stmt.setString(3, dept.getLocation());
            stmt.setDouble(4, dept.getBudget());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        dept.setDepartmentId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to save department: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int getEmployeeCountByDepartment(int departmentId) {
        String sql = "SELECT COUNT(*) FROM employees WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to count employees in dept: " + e.getMessage());
        }
        return 0;
    }
}

