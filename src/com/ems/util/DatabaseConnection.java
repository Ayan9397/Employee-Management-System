package com.ems.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Singleton database connection manager utilizing JDBC.
 * Demonstrates:
 * 1. JDBC Driver Loading
 * 2. Connection establishment with MySQL
 * 3. Schema auto-verification & error handling
 */
public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;
    private static boolean driverLoaded = false;

    static {
        loadConfiguration();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            System.err.println("[WARN] MySQL JDBC Driver not found on classpath: " + e.getMessage());
        }
    }

    private DatabaseConnection() {
        // Utility singleton
    }

    public static void loadConfiguration() {
        Properties props = new Properties();
        File propFile = new File("db.properties");
        if (propFile.exists()) {
            try (InputStream in = new FileInputStream(propFile)) {
                props.load(in);
            } catch (Exception e) {
                System.err.println("[WARN] Failed to load db.properties: " + e.getMessage());
            }
        }

        String defaultUrl = "jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true";
        String configUrl = props.getProperty("db.url", defaultUrl);
        String configUser = props.getProperty("db.user", "root");
        String configPass = props.getProperty("db.password", "root");

        // Environment variables take precedence (Docker / Cloud Deployments)
        String envUrl = System.getenv("DB_URL");
        String envUser = System.getenv("DB_USER");
        String envPass = System.getenv("DB_PASSWORD");

        url = (envUrl != null && !envUrl.trim().isEmpty()) ? envUrl : configUrl;
        user = (envUser != null && !envUser.trim().isEmpty()) ? envUser : configUser;
        password = (envPass != null) ? envPass : configPass;
    }

    public static void setCredentials(String newUrl, String newUser, String newPassword) {
        url = newUrl;
        user = newUser;
        password = newPassword;
    }

    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("MySQL JDBC Driver is not loaded.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Automatically initializes essential tables if they do not exist in MySQL.
     */
    public static void initializeDatabase() {
        if (!testConnection()) return;

        String createDept = "CREATE TABLE IF NOT EXISTS departments (" +
                "department_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "department_code VARCHAR(20) NOT NULL UNIQUE, " +
                "department_name VARCHAR(100) NOT NULL, " +
                "location VARCHAR(100) NOT NULL, " +
                "budget DECIMAL(15,2) DEFAULT 0.00, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        String createRoles = "CREATE TABLE IF NOT EXISTS roles (" +
                "role_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "role_title VARCHAR(100) NOT NULL UNIQUE, " +
                "description VARCHAR(255), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        String createEmp = "CREATE TABLE IF NOT EXISTS employees (" +
                "employee_id VARCHAR(20) PRIMARY KEY, " +
                "first_name VARCHAR(50) NOT NULL, " +
                "last_name VARCHAR(50) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "phone VARCHAR(20) NOT NULL, " +
                "hire_date DATE NOT NULL, " +
                "department_id INT, " +
                "role_id INT, " +
                "employment_type ENUM('FULL_TIME', 'CONTRACT') NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL, " +
                "FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE SET NULL)";

        String createSalary = "CREATE TABLE IF NOT EXISTS salary (" +
                "salary_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "employee_id VARCHAR(20) NOT NULL UNIQUE, " +
                "base_or_annual_salary DECIMAL(12, 2) DEFAULT 0.00, " +
                "hourly_rate DECIMAL(10, 2) DEFAULT 0.00, " +
                "hours_worked DECIMAL(6, 2) DEFAULT 0.00, " +
                "bonus DECIMAL(10, 2) DEFAULT 0.00, " +
                "deductions DECIMAL(10, 2) DEFAULT 0.00, " +
                "net_monthly_salary DECIMAL(12, 2) NOT NULL, " +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE)";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createDept);
            stmt.execute(createRoles);
            stmt.execute(createEmp);
            stmt.execute(createSalary);
        } catch (SQLException e) {
            System.err.println("[WARN] Database auto-initialization error: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }
}

