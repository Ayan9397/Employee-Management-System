package com.ems.dao.jdbc;

import com.ems.dao.EmployeeRepository;
import com.ems.model.ContractEmployee;
import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;
import com.ems.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of EmployeeRepository.
 * Demonstrates:
 * 1. JDBC PreparedStatements & Parameter Binding.
 * 2. Multi-table JOIN queries (employees + departments + roles + salary).
 * 3. Polymorphic Object Relational Mapping (mapping ResultSets to FullTime/Contract objects).
 * 4. ACID Transactions (atomic insert/update across employees and salary).
 */
public class JdbcEmployeeRepositoryImpl implements EmployeeRepository {

    private static final String BASE_SELECT_QUERY =
            "SELECT e.employee_id, e.first_name, e.last_name, e.email, e.phone, e.hire_date, " +
            "       e.department_id, d.department_name, e.role_id, r.role_title, e.employment_type, " +
            "       s.base_or_annual_salary, s.hourly_rate, s.hours_worked, s.bonus, s.deductions, s.net_monthly_salary " +
            "FROM employees e " +
            "LEFT JOIN departments d ON e.department_id = d.department_id " +
            "LEFT JOIN roles r ON e.role_id = r.role_id " +
            "LEFT JOIN salary s ON e.employee_id = s.employee_id ";

    @Override
    public boolean save(Employee employee) {
        String insertEmpSql = "INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, department_id, role_id, employment_type) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertSalarySql = "INSERT INTO salary (employee_id, base_or_annual_salary, hourly_rate, hours_worked, bonus, deductions, net_monthly_salary) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            // 1. Insert Employee Record
            try (PreparedStatement empStmt = conn.prepareStatement(insertEmpSql)) {
                empStmt.setString(1, employee.getEmployeeId());
                empStmt.setString(2, employee.getFirstName());
                empStmt.setString(3, employee.getLastName());
                empStmt.setString(4, employee.getEmail());
                empStmt.setString(5, employee.getPhone());
                empStmt.setDate(6, Date.valueOf(employee.getHireDate() != null ? employee.getHireDate() : LocalDate.now()));
                if (employee.getDepartmentId() > 0) {
                    empStmt.setInt(7, employee.getDepartmentId());
                } else {
                    empStmt.setNull(7, Types.INTEGER);
                }
                if (employee.getRoleId() > 0) {
                    empStmt.setInt(8, employee.getRoleId());
                } else {
                    empStmt.setNull(8, Types.INTEGER);
                }
                empStmt.setString(9, employee.getEmploymentType());
                empStmt.executeUpdate();
            }

            // 2. Insert Salary Record based on polymorphic type
            try (PreparedStatement salStmt = conn.prepareStatement(insertSalarySql)) {
                salStmt.setString(1, employee.getEmployeeId());
                if (employee instanceof FullTimeEmployee) {
                    FullTimeEmployee ft = (FullTimeEmployee) employee;
                    salStmt.setDouble(2, ft.getAnnualSalary());
                    salStmt.setDouble(3, 0.0);
                    salStmt.setDouble(4, 0.0);
                    salStmt.setDouble(5, ft.getMonthlyBonus());
                    salStmt.setDouble(6, ft.getMonthlyDeductions());
                } else if (employee instanceof ContractEmployee) {
                    ContractEmployee ct = (ContractEmployee) employee;
                    salStmt.setDouble(2, 0.0);
                    salStmt.setDouble(3, ct.getHourlyRate());
                    salStmt.setDouble(4, ct.getHoursWorked());
                    salStmt.setDouble(5, 0.0);
                    salStmt.setDouble(6, ct.getMonthlyDeductions());
                }
                salStmt.setDouble(7, employee.calculateMonthlySalary());
                salStmt.executeUpdate();
            }

            conn.commit(); // Commit Transaction
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to save employee: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean update(Employee employee) {
        String updateEmpSql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone = ?, " +
                              "department_id = ?, role_id = ? WHERE employee_id = ?";

        String updateSalarySql = "UPDATE salary SET base_or_annual_salary = ?, hourly_rate = ?, hours_worked = ?, " +
                                 "bonus = ?, deductions = ?, net_monthly_salary = ? WHERE employee_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement empStmt = conn.prepareStatement(updateEmpSql)) {
                empStmt.setString(1, employee.getFirstName());
                empStmt.setString(2, employee.getLastName());
                empStmt.setString(3, employee.getEmail());
                empStmt.setString(4, employee.getPhone());
                if (employee.getDepartmentId() > 0) {
                    empStmt.setInt(5, employee.getDepartmentId());
                } else {
                    empStmt.setNull(5, Types.INTEGER);
                }
                if (employee.getRoleId() > 0) {
                    empStmt.setInt(6, employee.getRoleId());
                } else {
                    empStmt.setNull(6, Types.INTEGER);
                }
                empStmt.setString(7, employee.getEmployeeId());
                empStmt.executeUpdate();
            }

            try (PreparedStatement salStmt = conn.prepareStatement(updateSalarySql)) {
                if (employee instanceof FullTimeEmployee) {
                    FullTimeEmployee ft = (FullTimeEmployee) employee;
                    salStmt.setDouble(1, ft.getAnnualSalary());
                    salStmt.setDouble(2, 0.0);
                    salStmt.setDouble(3, 0.0);
                    salStmt.setDouble(4, ft.getMonthlyBonus());
                    salStmt.setDouble(5, ft.getMonthlyDeductions());
                } else if (employee instanceof ContractEmployee) {
                    ContractEmployee ct = (ContractEmployee) employee;
                    salStmt.setDouble(1, 0.0);
                    salStmt.setDouble(2, ct.getHourlyRate());
                    salStmt.setDouble(3, ct.getHoursWorked());
                    salStmt.setDouble(4, 0.0);
                    salStmt.setDouble(5, ct.getMonthlyDeductions());
                }
                salStmt.setDouble(6, employee.calculateMonthlySalary());
                salStmt.setString(7, employee.getEmployeeId());
                salStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update employee: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean delete(String employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to delete employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Employee findById(String employeeId) {
        String sql = BASE_SELECT_QUERY + "WHERE e.employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to find employee by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = BASE_SELECT_QUERY + "ORDER BY e.created_at ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve employees: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Employee> search(String keyword) {
        List<Employee> list = new ArrayList<>();
        String sql = BASE_SELECT_QUERY +
                     "WHERE LOWER(e.employee_id) LIKE ? " +
                     "   OR LOWER(e.first_name) LIKE ? " +
                     "   OR LOWER(e.last_name) LIKE ? " +
                     "   OR LOWER(e.email) LIKE ? " +
                     "   OR LOWER(d.department_name) LIKE ? " +
                     "ORDER BY e.first_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword.toLowerCase().trim() + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, pattern);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Search error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Employee> findByDepartment(int departmentId) {
        List<Employee> list = new ArrayList<>();
        String sql = BASE_SELECT_QUERY + "WHERE e.department_id = ? ORDER BY e.first_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Find by department error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int countTotalEmployees() {
        String sql = "SELECT COUNT(*) FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Count query error: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public String generateNextEmployeeId(String employmentType) {
        String prefix = "FULL_TIME".equalsIgnoreCase(employmentType) ? "EMP-FT-" : "EMP-CT-";
        int baseIndex = "FULL_TIME".equalsIgnoreCase(employmentType) ? 1000 : 2000;

        String sql = "SELECT employee_id FROM employees WHERE employment_type = ? ORDER BY employee_id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employmentType.toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String lastId = rs.getString("employee_id");
                    String numPart = lastId.substring(lastId.lastIndexOf('-') + 1);
                    int current = Integer.parseInt(numPart);
                    return prefix + (current + 1);
                }
            }
        } catch (Exception e) {
            // fallback
        }
        return prefix + (baseIndex + countTotalEmployees() + 1);
    }

    /**
     * Polymorphically maps a SQL ResultSet row to the appropriate Employee subclass.
     */
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        String empType = rs.getString("employment_type");
        String empId = rs.getString("employee_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        Date hDate = rs.getDate("hire_date");
        LocalDate hireDate = (hDate != null) ? hDate.toLocalDate() : LocalDate.now();
        int deptId = rs.getInt("department_id");
        String deptName = rs.getString("department_name");
        int roleId = rs.getInt("role_id");
        String roleTitle = rs.getString("role_title");

        double baseSalary = rs.getDouble("base_or_annual_salary");
        double hourlyRate = rs.getDouble("hourly_rate");
        double hoursWorked = rs.getDouble("hours_worked");
        double bonus = rs.getDouble("bonus");
        double deductions = rs.getDouble("deductions");

        Employee emp;
        if ("FULL_TIME".equalsIgnoreCase(empType)) {
            emp = new FullTimeEmployee(empId, firstName, lastName, email, phone, hireDate,
                    deptId, roleId, baseSalary, bonus, deductions, "Standard Health, Dental & 401(k)");
        } else {
            emp = new ContractEmployee(empId, firstName, lastName, email, phone, hireDate,
                    deptId, roleId, hourlyRate, hoursWorked, 6, deductions);
        }

        emp.setDepartmentName(deptName != null ? deptName : "Unassigned");
        emp.setRoleTitle(roleTitle != null ? roleTitle : "Unassigned");
        return emp;
    }
}

