package com.ems.dao.jdbc;

import com.ems.dao.SalaryRepository;
import com.ems.model.SalaryRecord;
import com.ems.util.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JDBC Implementation of SalaryRepository.
 * Demonstrates:
 * 1. SQL Aggregations (SUM, AVG, MIN, MAX).
 * 2. GROUP BY queries with multi-table JOINs.
 * 3. Upsert operations with PreparedStatement.
 */
public class JdbcSalaryRepositoryImpl implements SalaryRepository {

    @Override
    public SalaryRecord findByEmployeeId(String employeeId) {
        String sql = "SELECT * FROM salary WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SalaryRecord(
                            rs.getInt("salary_id"),
                            rs.getString("employee_id"),
                            rs.getDouble("base_or_annual_salary"),
                            rs.getDouble("hourly_rate"),
                            rs.getDouble("hours_worked"),
                            rs.getDouble("bonus"),
                            rs.getDouble("deductions"),
                            rs.getDouble("net_monthly_salary"),
                            rs.getTimestamp("last_updated") != null ? rs.getTimestamp("last_updated").toLocalDateTime() : null
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find salary record: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(SalaryRecord r) {
        String checkSql = "SELECT salary_id FROM salary WHERE employee_id = ?";
        String updateSql = "UPDATE salary SET base_or_annual_salary = ?, hourly_rate = ?, hours_worked = ?, " +
                           "bonus = ?, deductions = ?, net_monthly_salary = ? WHERE employee_id = ?";
        String insertSql = "INSERT INTO salary (employee_id, base_or_annual_salary, hourly_rate, hours_worked, " +
                           "bonus, deductions, net_monthly_salary) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, r.getEmployeeId());
            boolean exists;
            try (ResultSet rs = checkStmt.executeQuery()) {
                exists = rs.next();
            }

            if (exists) {
                try (PreparedStatement upd = conn.prepareStatement(updateSql)) {
                    upd.setDouble(1, r.getBaseOrAnnualSalary());
                    upd.setDouble(2, r.getHourlyRate());
                    upd.setDouble(3, r.getHoursWorked());
                    upd.setDouble(4, r.getBonus());
                    upd.setDouble(5, r.getDeductions());
                    upd.setDouble(6, r.getNetMonthlySalary());
                    upd.setString(7, r.getEmployeeId());
                    return upd.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setString(1, r.getEmployeeId());
                    ins.setDouble(2, r.getBaseOrAnnualSalary());
                    ins.setDouble(3, r.getHourlyRate());
                    ins.setDouble(4, r.getHoursWorked());
                    ins.setDouble(5, r.getBonus());
                    ins.setDouble(6, r.getDeductions());
                    ins.setDouble(7, r.getNetMonthlySalary());
                    return ins.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Save or update salary failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Demonstrates SQL Aggregation & GROUP BY queries.
     */
    @Override
    public Map<String, Object> getSalaryStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Overall stats
        String overallSql = "SELECT COUNT(*) AS total_records, " +
                            "       COALESCE(SUM(net_monthly_salary), 0) AS total_payroll, " +
                            "       COALESCE(AVG(net_monthly_salary), 0) AS avg_salary, " +
                            "       COALESCE(MIN(net_monthly_salary), 0) AS min_salary, " +
                            "       COALESCE(MAX(net_monthly_salary), 0) AS max_salary " +
                            "FROM salary";

        // Group by department breakdown
        String deptBreakdownSql = "SELECT d.department_name, " +
                                  "       COUNT(e.employee_id) AS emp_count, " +
                                  "       COALESCE(SUM(s.net_monthly_salary), 0) AS dept_payroll, " +
                                  "       COALESCE(AVG(s.net_monthly_salary), 0) AS dept_avg " +
                                  "FROM departments d " +
                                  "LEFT JOIN employees e ON d.department_id = e.department_id " +
                                  "LEFT JOIN salary s ON e.employee_id = s.employee_id " +
                                  "GROUP BY d.department_id, d.department_name " +
                                  "ORDER BY dept_payroll DESC";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(overallSql)) {
                if (rs.next()) {
                    stats.put("totalRecords", rs.getInt("total_records"));
                    stats.put("totalMonthlyPayroll", rs.getDouble("total_payroll"));
                    stats.put("averageMonthlySalary", rs.getDouble("avg_salary"));
                    stats.put("minMonthlySalary", rs.getDouble("min_salary"));
                    stats.put("maxMonthlySalary", rs.getDouble("max_salary"));
                }
            }

            Map<String, double[]> deptStats = new LinkedHashMap<>();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(deptBreakdownSql)) {
                while (rs.next()) {
                    String dept = rs.getString("department_name");
                    int count = rs.getInt("emp_count");
                    double sum = rs.getDouble("dept_payroll");
                    double avg = rs.getDouble("dept_avg");
                    deptStats.put(dept, new double[]{count, sum, avg});
                }
            }
            stats.put("departmentBreakdown", deptStats);

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch salary statistics: " + e.getMessage());
        }

        return stats;
    }
}

