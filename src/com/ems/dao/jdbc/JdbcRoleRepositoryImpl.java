package com.ems.dao.jdbc;

import com.ems.dao.RoleRepository;
import com.ems.model.Role;
import com.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of RoleRepository.
 */
public class JdbcRoleRepositoryImpl implements RoleRepository {

    @Override
    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM roles ORDER BY role_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Role(
                        rs.getInt("role_id"),
                        rs.getString("role_title"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch roles: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Role findById(int roleId) {
        String sql = "SELECT * FROM roles WHERE role_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(
                            rs.getInt("role_id"),
                            rs.getString("role_title"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch role: " + e.getMessage());
        }
        return null;
    }
}

